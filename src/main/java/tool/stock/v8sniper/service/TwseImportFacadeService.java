package tool.stock.v8sniper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.ImportSummary;
import tool.stock.v8sniper.entity.ImportBatchEntity;
import tool.stock.v8sniper.entity.StockBasicEntity;
import tool.stock.v8sniper.entity.StockChipDailyEntity;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;
import tool.stock.v8sniper.entity.StockValuationDailyEntity;
import tool.stock.v8sniper.repository.V8StockBasicRepository;
import tool.stock.v8sniper.repository.StockChipDailyRepository;
import tool.stock.v8sniper.repository.StockDailyQuoteRepository;
import tool.stock.v8sniper.repository.StockValuationDailyRepository;
import tool.stock.v8sniper.util.JsonFieldExtractor;
import tool.stock.v8sniper.util.MathUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TwseImportFacadeService {
    private final TwseOpenApiClient apiClient;
    private final FileStorageService fileStorageService;
    private final ImportBatchService importBatchService;
    private final ObjectMapper objectMapper;
    private final V8StockBasicRepository stockBasicRepository;
    private final StockDailyQuoteRepository quoteRepository;
    private final StockValuationDailyRepository valuationRepository;
    private final StockChipDailyRepository chipRepository;
    private final JdbcTemplate jdbcTemplate;
    private final V8Properties properties;

    public TwseImportFacadeService(TwseOpenApiClient apiClient, FileStorageService fileStorageService, ImportBatchService importBatchService,
                                   ObjectMapper objectMapper, V8StockBasicRepository stockBasicRepository,
                                   StockDailyQuoteRepository quoteRepository, StockValuationDailyRepository valuationRepository,
                                   StockChipDailyRepository chipRepository, JdbcTemplate jdbcTemplate, V8Properties properties) {
        this.apiClient = apiClient; this.fileStorageService = fileStorageService; this.importBatchService = importBatchService; this.objectMapper = objectMapper;
        this.stockBasicRepository = stockBasicRepository; this.quoteRepository = quoteRepository; this.valuationRepository = valuationRepository;
        this.chipRepository = chipRepository; this.jdbcTemplate = jdbcTemplate; this.properties = properties;
    }

    @Transactional
    public List<ImportSummary> importDaily(LocalDate date) {
        List<ImportSummary> result = new ArrayList<>();
        result.add(importDataset(date, "STOCK_BASIC", properties.getTwse().getStockBasicEndpoint(), "twse_stock_basic.json"));
        result.add(importDataset(date, "DAILY_QUOTE", properties.getTwse().getDailyQuoteEndpoint(), "twse_daily_quote.json"));
        result.add(importDataset(date, "VALUATION", properties.getTwse().getValuationEndpoint(), "twse_valuation.json"));
        result.add(importDataset(date, "CHIP", properties.getTwse().getChipEndpoint(), "twse_chip.json"));
        return result;
    }

    public List<ImportSummary> importRange(LocalDate start, LocalDate end) {
        List<ImportSummary> summaries = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            summaries.addAll(importDaily(current));
            current = current.plusDays(1);
        }
        return summaries;
    }

    private ImportSummary importDataset(LocalDate date, String sourceType, String endpoint, String fileName) {
        try {
            String body = loadSource(date, endpoint, fileName);
            Path file = fileStorageService.rawFile(date, fileName);
            ImportBatchEntity batch = importBatchService.start(date, sourceType, properties.getTwse().getBaseUrl() + endpoint, file.getFileName().toString(), file.toAbsolutePath().toString());
            JsonNode root = objectMapper.readTree(body);
            if (!root.isArray()) {
                throw new IllegalStateException("TWSE 回傳不是 JSON array");
            }
            int rows = switch (sourceType) {
                case "STOCK_BASIC" -> importStockBasic(root);
                case "DAILY_QUOTE" -> importDailyQuote(date, root, batch.getId());
                case "VALUATION" -> importValuation(date, root);
                case "CHIP" -> importChip(date, root);
                default -> 0;
            };
            importBatchService.success(batch, rows);
            return new ImportSummary(sourceType, rows, batch.getId(), "完成");
        } catch (Exception ex) {
            ImportBatchEntity batch = importBatchService.start(date, sourceType, properties.getTwse().getBaseUrl() + endpoint, fileName, "N/A");
            importBatchService.fail(batch, 0, 0, ex.getMessage());
            return new ImportSummary(sourceType, 0, batch.getId(), "失敗: " + ex.getMessage());
        }
    }

    private String loadSource(LocalDate date, String endpoint, String fileName) throws IOException {
        Path file = fileStorageService.rawFile(date, fileName);
        String mode = properties.getImportMode();
        if (Files.exists(file) && ("FILE_ONLY".equalsIgnoreCase(mode) || "API_AND_FILE".equalsIgnoreCase(mode))) {
            return Files.readString(file);
        }
        if ("FILE_ONLY".equalsIgnoreCase(mode)) {
            throw new IllegalStateException("找不到本地檔案: " + file);
        }
        String body = apiClient.fetch(endpoint);
        Files.writeString(file, body == null ? "" : body);
        return body;
    }

    private int importStockBasic(JsonNode root) {
        int count = 0;
        for (JsonNode row : root) {
            String code = JsonFieldExtractor.text(row, "公司代號", "stockCode", "Code");
            String name = JsonFieldExtractor.text(row, "公司簡稱", "公司名稱", "Name");
            if (code == null || name == null) continue;
            StockBasicEntity entity = stockBasicRepository.findByStockCode(code).orElseGet(StockBasicEntity::new);
            entity.setStockCode(code); entity.setStockName(name);
            entity.setIndustry(JsonFieldExtractor.text(row, "產業別", "industry"));
            entity.setMarketType("TWSE"); entity.setListingType("上市"); entity.setEtf(code.startsWith("00"));
            stockBasicRepository.save(entity); count++;
        }
        return count;
    }

    private int importDailyQuote(LocalDate date, JsonNode root, Long batchId) {
        jdbcTemplate.update("DELETE FROM stock_daily_quote_staging WHERE batch_id = ?", batchId);
        int count = 0;
        for (JsonNode row : root) {
            String code = JsonFieldExtractor.text(row, "Code", "證券代號");
            String name = JsonFieldExtractor.text(row, "Name", "證券名稱");
            if (code == null) continue;
            jdbcTemplate.update("INSERT INTO stock_daily_quote_staging(batch_id, trade_date, stock_code, stock_name, raw_json) VALUES (?,?,?,?,?)",
                    batchId, date, code, name, row.toString());
            StockDailyQuoteEntity entity = quoteRepository.findByTradeDateAndStockCode(date, code).orElseGet(StockDailyQuoteEntity::new);
            entity.setTradeDate(date); entity.setStockCode(code); entity.setOpenPrice(JsonFieldExtractor.decimal(row, "OpeningPrice", "開盤價"));
            entity.setHighPrice(JsonFieldExtractor.decimal(row, "HighestPrice", "最高價")); entity.setLowPrice(JsonFieldExtractor.decimal(row, "LowestPrice", "最低價"));
            entity.setClosePrice(JsonFieldExtractor.decimal(row, "ClosingPrice", "收盤價")); entity.setPriceChange(JsonFieldExtractor.decimal(row, "Change", "漲跌價差"));
            entity.setVolume(JsonFieldExtractor.longValue(row, "TradeVolume", "成交股數", "成交量")); entity.setTurnover(JsonFieldExtractor.decimal(row, "TradeValue", "成交金額"));
            entity.setTrades(JsonFieldExtractor.longValue(row, "Transaction", "成交筆數")); entity.setSourceType(properties.getImportMode()); entity.setRawBatchId(batchId);
            if (entity.getClosePrice() != null) {
                StockDailyQuoteEntity prev = quoteRepository.findByStockCodeOrderByTradeDateAsc(code).stream().filter(x -> x.getTradeDate().isBefore(date)).reduce((a, b) -> b).orElse(null);
                entity.setPctChange(prev == null ? null : MathUtil.pctChange(entity.getClosePrice(), prev.getClosePrice(), 4));
            }
            quoteRepository.save(entity);
            stockBasicRepository.findByStockCode(code).ifPresentOrElse(b -> {}, () -> {
                StockBasicEntity basic = new StockBasicEntity(); basic.setStockCode(code); basic.setStockName(name == null ? code : name); basic.setIndustry("未分類"); basic.setMarketType("TWSE"); basic.setListingType("上市"); basic.setEtf(code.startsWith("00")); stockBasicRepository.save(basic);
            });
            count++;
        }
        return count;
    }

    private int importValuation(LocalDate date, JsonNode root) {
        int count = 0;
        for (JsonNode row : root) {
            String code = JsonFieldExtractor.text(row, "Code", "證券代號"); if (code == null) continue;
            StockValuationDailyEntity entity = valuationRepository.findByTradeDateAndStockCode(date, code).orElseGet(StockValuationDailyEntity::new);
            entity.setTradeDate(date); entity.setStockCode(code); entity.setPeRatio(JsonFieldExtractor.decimal(row, "PEratio", "本益比"));
            entity.setDividendYield(JsonFieldExtractor.decimal(row, "DividendYield", "殖利率", "殖利率(%)")); entity.setPbRatio(JsonFieldExtractor.decimal(row, "PBratio", "股價淨值比")); entity.setEps(null);
            valuationRepository.save(entity); count++;
        }
        return count;
    }

    private int importChip(LocalDate date, JsonNode root) {
        int count = 0;
        for (JsonNode row : root) {
            String code = JsonFieldExtractor.text(row, "Code", "證券代號"); if (code == null) continue;
            StockChipDailyEntity entity = chipRepository.findByTradeDateAndStockCode(date, code).orElseGet(StockChipDailyEntity::new);
            entity.setTradeDate(date); entity.setStockCode(code);
            entity.setForeignNetBuy(JsonFieldExtractor.longValue(row, "外陸資買賣超股數(不含外資自營商)", "外資及陸資買賣超股數", "外資買賣超股數"));
            entity.setInvestmentTrustNetBuy(JsonFieldExtractor.longValue(row, "投信買賣超股數", "投信買賣超"));
            entity.setDealerNetBuy(JsonFieldExtractor.longValue(row, "自營商買賣超股數", "自行買賣買賣超股數", "避險買賣超股數"));
            entity.setMarginBalance(null); entity.setShortBalance(null); // 官方資料來源目前無此欄位，先保留 schema。
            chipRepository.save(entity); count++;
        }
        return count;
    }
}

package tool.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.entity.ImportBatch;
import tool.stock.entity.StockBasic;
import tool.stock.entity.StockPriceDaily;
import tool.stock.repository.ImportBatchRepository;
import tool.stock.repository.StockBasicRepository;
import tool.stock.repository.StockPriceDailyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 匯入 TWSE 日線資料。
 */
@Service
public class TwseImportService {

    private static final Logger log = LoggerFactory.getLogger(TwseImportService.class);

    private final TwseApiClient twseApiClient;
    private final StockBasicRepository stockBasicRepository;
    private final StockPriceDailyRepository stockPriceDailyRepository;
    private final ImportBatchRepository importBatchRepository;

    public TwseImportService(TwseApiClient twseApiClient,
                             StockBasicRepository stockBasicRepository,
                             StockPriceDailyRepository stockPriceDailyRepository,
                             ImportBatchRepository importBatchRepository) {
        this.twseApiClient = twseApiClient;
        this.stockBasicRepository = stockBasicRepository;
        this.stockPriceDailyRepository = stockPriceDailyRepository;
        this.importBatchRepository = importBatchRepository;
    }

    @Transactional
    public int importBasicData() {
        String apiPath = "/opendata/t187ap03_L";
        ImportBatch batch = createBatch("STOCK_BASIC", apiPath);
        try {
            JsonNode root = twseApiClient.getArray(apiPath);
            List<StockBasic> entities = new ArrayList<>();
            for (JsonNode row : root) {
                String stockCode = readText(row, "公司代號", "stock_code", "Code");
                String stockName = readText(row, "公司簡稱", "公司名稱", "stock_name", "Name");
                if (isBlank(stockCode) || isBlank(stockName)) {
                    continue;
                }

                StockBasic entity = stockBasicRepository.findByStockCode(stockCode).orElseGet(StockBasic::new);
                entity.setStockCode(stockCode.trim());
                entity.setStockName(stockName.trim());
                entity.setMarketType(readText(row, "市場別", "市場別代號", "market_type"));
                entity.setIndustryType(readText(row, "產業別", "industry_type"));
                entity.setIsinCode(readText(row, "國際證券辨識號碼", "ISIN", "isin_code"));
                entity.setListingDate(parseDate(readText(row, "上市日", "上市日期", "listing_date")));
                entities.add(entity);
            }
            stockBasicRepository.saveAll(entities);
            completeBatch(batch, root.size(), entities.size(), Math.max(root.size() - entities.size(), 0), null);
            return entities.size();
        } catch (Exception e) {
            failBatch(batch, e);
            throw e;
        }
    }

    @Transactional
    public int importDailyPrices(LocalDate tradeDate) {
        String apiPath = "/exchangeReport/STOCK_DAY_ALL";
        ImportBatch batch = createBatch("STOCK_DAY_ALL", apiPath);
        try {
            JsonNode root = twseApiClient.getArray(apiPath);
            List<StockPriceDaily> entities = new ArrayList<>();
            for (JsonNode row : root) {
                String stockCode = readText(row, "Code", "證券代號", "code");
                String stockName = readText(row, "Name", "證券名稱", "name");
                if (isBlank(stockCode) || isBlank(stockName)) {
                    continue;
                }

                StockPriceDaily entity = stockPriceDailyRepository.findByStockCodeAndTradeDate(stockCode, tradeDate)
                        .orElseGet(StockPriceDaily::new);
                entity.setStockCode(stockCode.trim());
                entity.setStockName(stockName.trim());
                entity.setTradeDate(tradeDate);
                entity.setOpenPrice(readDecimal(row, "OpeningPrice", "開盤價", "open_price"));
                entity.setHighPrice(readDecimal(row, "HighestPrice", "最高價", "high_price"));
                entity.setLowPrice(readDecimal(row, "LowestPrice", "最低價", "low_price"));
                entity.setClosePrice(readDecimal(row, "ClosingPrice", "收盤價", "close_price"));
                entity.setVolume(readLong(row, "TradeVolume", "成交股數", "成交量", "volume"));
                entity.setTurnover(readDecimal(row, "TradeValue", "成交金額", "turnover_amount"));

                if (entity.getOpenPrice() == null || entity.getHighPrice() == null || entity.getLowPrice() == null
                        || entity.getClosePrice() == null || entity.getVolume() == null || entity.getTurnover() == null) {
                    continue;
                }
                entities.add(entity);
            }
            stockPriceDailyRepository.saveAll(entities);
            completeBatch(batch, root.size(), entities.size(), Math.max(root.size() - entities.size(), 0), null);
            log.info("TWSE 日線匯入完成，tradeDate={}, count={}", tradeDate, entities.size());
            return entities.size();
        } catch (Exception e) {
            failBatch(batch, e);
            throw new IllegalStateException("TWSE 日線匯入失敗", e);
        }
    }

    private ImportBatch createBatch(String sourceType, String apiPath) {
        ImportBatch batch = new ImportBatch();
        batch.setBatchDate(LocalDate.now());
        batch.setSourceType(sourceType);
        batch.setApiPath(apiPath);
        batch.setImportStatus("RUNNING");
        batch.setTotalRows(0);
        batch.setSuccessRows(0);
        batch.setFailRows(0);
        return importBatchRepository.save(batch);
    }

    private void completeBatch(ImportBatch batch, int totalRows, int successRows, int failRows, String errorMessage) {
        batch.setImportStatus(failRows > 0 ? "PARTIAL_SUCCESS" : "SUCCESS");
        batch.setTotalRows(totalRows);
        batch.setSuccessRows(successRows);
        batch.setFailRows(failRows);
        batch.setErrorMessage(errorMessage);
        importBatchRepository.save(batch);
    }

    private void failBatch(ImportBatch batch, Exception e) {
        batch.setImportStatus("FAILED");
        batch.setErrorMessage(left(e.getMessage(), 2000));
        importBatchRepository.save(batch);
    }

    private String readText(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode value = node.get(key);
            if (value != null) {
                String text = value.asText();
                if (!isBlank(text) && !"--".equals(text)) {
                    return text.trim();
                }
            }
        }
        return null;
    }

    private BigDecimal readDecimal(JsonNode node, String... keys) {
        String text = readText(node, keys);
        if (isBlank(text)) {
            return null;
        }
        String normalized = text.replace(",", "").replace("X", "").replace("+", "").trim();
        if (normalized.isBlank() || "-".equals(normalized)) {
            return null;
        }
        try {
            return new BigDecimal(normalized);
        } catch (Exception e) {
            log.debug("decimal parse 失敗: {}", text);
            return null;
        }
    }

    private Long readLong(JsonNode node, String... keys) {
        BigDecimal decimal = readDecimal(node, keys);
        return decimal == null ? null : decimal.longValue();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private LocalDate parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        String normalized = value.replace("/", "").replace("-", "").trim();
        if (!normalized.matches("\\d{8}")) {
            return null;
        }
        return LocalDate.of(
                Integer.parseInt(normalized.substring(0, 4)),
                Integer.parseInt(normalized.substring(4, 6)),
                Integer.parseInt(normalized.substring(6, 8))
        );
    }

    private String left(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}

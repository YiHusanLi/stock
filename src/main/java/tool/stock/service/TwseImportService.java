package tool.stock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

@Service
public class TwseImportService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.stock.auto-delete-after-import:true}")
    private boolean autoDeleteAfterImport;

    public TwseImportService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public int importFile(tool.stock.constant.TwseDataset dataset, Path filePath) {
        long batchId = insertBatchStart(dataset, filePath);
        System.out.println("正在匯入: " + dataset.name() + ", file=" + filePath);

        try {
            String content = Files.readString(filePath);

            String trimmed = content == null ? "" : content.trim();
            if (trimmed.startsWith("<")) {
                String preview = trimmed.substring(0, Math.min(300, trimmed.length()));
                throw new IllegalStateException("下載內容不是 JSON，疑似 HTML 頁面: " + preview);
            }

            JsonNode root = objectMapper.readTree(content);
            if (!root.isArray()) {
                throw new IllegalStateException("JSON 內容不是陣列: " + filePath);
            }

            LocalDate tradeDate = extractTradeDateFromFileName(filePath.getFileName().toString());

            int count = switch (dataset) {
                case STOCK_DAY_ALL -> importStockDayAll(root, filePath.getFileName().toString(), tradeDate);
                case BWIBBU_ALL -> importBwibbuAll(root, filePath.getFileName().toString(), tradeDate);
                case T86 -> importT86(root, filePath.getFileName().toString(), tradeDate);
            };

            updateBatchSuccess(batchId, count);

            if (autoDeleteAfterImport) {
                Files.deleteIfExists(filePath);
            }

            return count;
        } catch (Exception e) {

            System.out.println("=== 匯入失敗詳細錯誤 ===");
            e.printStackTrace();

            updateBatchFailed(batchId, e.toString());

            throw new RuntimeException("匯入失敗: " + dataset.name() + ", file=" + filePath, e);
        }
    }

    private int importStockDayAll(JsonNode root, String sourceFileName, LocalDate tradeDate) {
        int count = 0;
        Iterator<JsonNode> iterator = root.iterator();

        while (iterator.hasNext()) {
            JsonNode row = iterator.next();

            String stockCode = text(row, "證券代號", "Code", "code");
            String stockName = text(row, "證券名稱", "Name", "name");

            if (isBlank(stockCode)) {
                continue;
            }

            upsertSecurityMaster(stockCode, stockName, guessMarketType(stockCode), null);

            jdbcTemplate.update("""
                INSERT INTO daily_price (
                    stock_code, trade_date, open_price, high_price, low_price, close_price,
                    volume, turnover_amount, source_file_name
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    open_price = VALUES(open_price),
                    high_price = VALUES(high_price),
                    low_price = VALUES(low_price),
                    close_price = VALUES(close_price),
                    volume = VALUES(volume),
                    turnover_amount = VALUES(turnover_amount),
                    source_file_name = VALUES(source_file_name),
                    updated_at = CURRENT_TIMESTAMP
            """,
                    stockCode,
                    tradeDate,
                    decimal(row, "開盤價", "open_price"),
                    decimal(row, "最高價", "high_price"),
                    decimal(row, "最低價", "low_price"),
                    decimal(row, "收盤價", "close_price"),
                    longVal(row, "成交股數", "成交量", "volume"),
                    decimal(row, "成交金額", "turnover_amount"),
                    sourceFileName
            );

            count++;
        }

        return count;
    }

    private int importBwibbuAll(JsonNode root, String sourceFileName, LocalDate tradeDate) {
        int count = 0;
        Iterator<JsonNode> iterator = root.iterator();

        while (iterator.hasNext()) {
            JsonNode row = iterator.next();

            String stockCode = text(row, "證券代號", "Code", "code");
            String stockName = text(row, "證券名稱", "Name", "name");

            if (isBlank(stockCode)) {
                continue;
            }

            upsertSecurityMaster(stockCode, stockName, guessMarketType(stockCode), null);

            jdbcTemplate.update("""
                INSERT INTO daily_valuation (
                    stock_code, trade_date, pe_ratio, eps, pb_ratio, dividend_yield, source_file_name
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    pe_ratio = VALUES(pe_ratio),
                    eps = VALUES(eps),
                    pb_ratio = VALUES(pb_ratio),
                    dividend_yield = VALUES(dividend_yield),
                    source_file_name = VALUES(source_file_name),
                    updated_at = CURRENT_TIMESTAMP
            """,
                    stockCode,
                    tradeDate,
                    decimal(row, "本益比", "PEratio", "pe_ratio"),
                    null,
                    decimal(row, "股價淨值比", "PBratio", "pb_ratio"),
                    decimal(row, "殖利率(%)", "殖利率", "DividendYield", "dividend_yield"),
                    sourceFileName
            );

            count++;
        }

        return count;
    }

    private int importT86(JsonNode root, String sourceFileName, LocalDate tradeDate) {

        int count = 0;

        for (JsonNode row : root) {

            try {
                String stockCode = text(row, "證券代號", "Code", "code");
                String stockName = text(row, "證券名稱", "Name", "name");

                if (isBlank(stockCode)) {
                    continue;
                }

                upsertSecurityMaster(stockCode, stockName, guessMarketType(stockCode), null);

                Long foreign = longVal(row,
                        "外陸資買賣超股數(不含外資自營商)",
                        "外資及陸資買賣超股數",
                        "外資買賣超股數");

                Long trust = longVal(row,
                        "投信買賣超股數",
                        "投信買賣超");

                Long dealer = longVal(row,
                        "自營商買賣超股數",
                        "自營商買賣超",
                        "自行買賣買賣超股數",
                        "避險買賣超股數");

                jdbcTemplate.update("""
                INSERT INTO daily_chip (
                    stock_code, trade_date,
                    foreign_net_buy, investment_trust_net_buy, dealer_net_buy,
                    source_file_name
                ) VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    foreign_net_buy = VALUES(foreign_net_buy),
                    investment_trust_net_buy = VALUES(investment_trust_net_buy),
                    dealer_net_buy = VALUES(dealer_net_buy),
                    source_file_name = VALUES(source_file_name),
                    updated_at = CURRENT_TIMESTAMP
            """,
                        stockCode,
                        tradeDate,
                        foreign,
                        trust,
                        dealer,
                        sourceFileName
                );

                count++;

            } catch (Exception e) {
                System.out.println("=== T86 單筆錯誤 ===");
                System.out.println(row.toString());
                e.printStackTrace();
            }
        }

        return count;
    }

    private void upsertSecurityMaster(String stockCode, String stockName, String marketType, String industryCategory) {
        jdbcTemplate.update("""
            INSERT INTO security_master (
                stock_code, stock_name, market_type, industry_category, is_active
            ) VALUES (?, ?, ?, ?, 1)
            ON DUPLICATE KEY UPDATE
                stock_name = VALUES(stock_name),
                market_type = VALUES(market_type),
                industry_category = COALESCE(VALUES(industry_category), industry_category),
                updated_at = CURRENT_TIMESTAMP
        """, stockCode, stockName, marketType, industryCategory);
    }

    private long insertBatchStart(tool.stock.constant.TwseDataset dataset, Path filePath) {
        jdbcTemplate.update("""
            INSERT INTO import_batch (
                batch_date, source_type, source_file_name, source_file_path,
                imported_table_name, import_status, total_rows, success_rows, failed_rows
            ) VALUES (?, ?, ?, ?, ?, 'PENDING', 0, 0, 0)
        """,
                LocalDate.now(),
                dataset.name(),
                filePath.getFileName().toString(),
                filePath.toAbsolutePath().toString(),
                guessTableName(dataset)
        );

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return id == null ? 0L : id;
    }

    private void updateBatchSuccess(long batchId, int successRows) {
        jdbcTemplate.update("""
            UPDATE import_batch
               SET import_status = 'SUCCESS',
                   total_rows = ?,
                   success_rows = ?,
                   failed_rows = 0,
                   updated_at = CURRENT_TIMESTAMP
             WHERE id = ?
        """, successRows, successRows, batchId);
    }

    private void updateBatchFailed(long batchId, String errorMessage) {
        jdbcTemplate.update("""
            UPDATE import_batch
               SET import_status = 'FAILED',
                   error_message = ?,
                   updated_at = CURRENT_TIMESTAMP
             WHERE id = ?
        """, left(errorMessage, 2000), batchId);
    }

    private String guessTableName(tool.stock.constant.TwseDataset dataset) {
        return switch (dataset) {
            case STOCK_DAY_ALL -> "daily_price";
            case BWIBBU_ALL -> "daily_valuation";
            case T86 -> "daily_chip";
        };
    }

    private LocalDate extractTradeDateFromFileName(String fileName) {
        String datePart = fileName.substring(0, 8);
        return LocalDate.parse(datePart, DateTimeFormatter.BASIC_ISO_DATE);
    }

    private String text(JsonNode row, String... keys) {
        for (String key : keys) {
            JsonNode value = row.get(key);
            if (value != null) {
                String str = value.asText();
                if (!isBlank(str)) {
                    return str.trim();
                }
            }
        }
        return null;
    }

    private BigDecimal decimal(JsonNode row, String... keys) {
        String value = text(row, keys);
        if (isBlank(value)) {
            return null;
        }

        value = value.replace(",", "").replace("%", "").trim();

        if ("--".equals(value) || "-".equals(value) || "X".equalsIgnoreCase(value)) {
            return null;
        }

        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Long longVal(JsonNode row, String... keys) {
        String value = text(row, keys);
        if (isBlank(value)) {
            return null;
        }

        value = value.replace(",", "").trim();

        if ("--".equals(value) || "-".equals(value) || "X".equalsIgnoreCase(value)) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String guessMarketType(String stockCode) {
        if (stockCode != null && stockCode.startsWith("00")) {
            return "ETF";
        }
        return "TW_STOCK";
    }

    private String left(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }
}
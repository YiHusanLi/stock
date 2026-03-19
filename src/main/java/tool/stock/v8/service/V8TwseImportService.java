package tool.stock.v8.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tool.stock.v8.entity.StockPriceDaily;
import tool.stock.v8.repository.StockPriceDailyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 匯入 TWSE 日線資料。
 * 欄位 mapping 集中在這個 service，後續若 TWSE 欄位改名只要改這裡。
 */
@Service
public class V8TwseImportService {

    private static final Logger log = LoggerFactory.getLogger(V8TwseImportService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final StockPriceDailyRepository stockPriceDailyRepository;

    @Value("${app.twse.base-url}")
    private String twseBaseUrl;

    public V8TwseImportService(RestTemplate restTemplate,
                               ObjectMapper objectMapper,
                               StockPriceDailyRepository stockPriceDailyRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.stockPriceDailyRepository = stockPriceDailyRepository;
    }

    @Transactional
    public int importDailyPrices(LocalDate tradeDate) {
        String url = twseBaseUrl + "/exchangeReport/STOCK_DAY_ALL";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("TWSE API 回傳空白內容");
        }

        try {
            JsonNode root = objectMapper.readTree(body);
            if (!root.isArray()) {
                throw new IllegalStateException("TWSE API 回傳格式不是陣列");
            }

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
            log.info("TWSE 日線匯入完成，tradeDate={}, count={}", tradeDate, entities.size());
            return entities.size();
        } catch (Exception e) {
            throw new IllegalStateException("TWSE 日線匯入失敗", e);
        }
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
}

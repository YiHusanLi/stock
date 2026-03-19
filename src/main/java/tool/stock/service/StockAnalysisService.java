package tool.stock.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StockAnalysisService {

    private final JdbcTemplate jdbcTemplate;

    public StockAnalysisService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> analyzeTopStocks() {
        Date latestDate = jdbcTemplate.queryForObject("""
            SELECT MAX(trade_date) FROM daily_price
        """, Date.class);

        if (latestDate == null) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> stocks = jdbcTemplate.queryForList("""
            SELECT dp.stock_code,
                   sm.stock_name
            FROM daily_price dp
            LEFT JOIN security_master sm
              ON dp.stock_code = sm.stock_code
            WHERE dp.trade_date = ?
            ORDER BY dp.stock_code
        """, latestDate);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> stock : stocks) {
            String code = stringValue(stock.get("stock_code"));
            String name = stringValue(stock.get("stock_name"));

            if (code == null || code.isBlank()) {
                continue;
            }

            List<Map<String, Object>> prices = jdbcTemplate.queryForList("""
                SELECT trade_date, open_price, high_price, low_price, close_price, volume, turnover_amount
                FROM daily_price
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 60
            """, code);

            if (prices.size() < 20) {
                continue;
            }

            List<Map<String, Object>> chips = jdbcTemplate.queryForList("""
                SELECT trade_date, foreign_net_buy, investment_trust_net_buy, dealer_net_buy
                FROM daily_chip
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 3
            """, code);

            Map<String, Object> latest = prices.get(0);

            List<Double> closes = extractDouble(prices, "close_price");
            List<Long> volumes = extractLong(prices, "volume");

            double ma5 = avg(closes, 5);
            double ma20 = avg(closes, 20);
            double ma60 = avg(closes, Math.min(60, closes.size()));

            double todayClose = getDouble(latest.get("close_price"));
            double todayVolume = getLong(latest.get("volume"));
            double avgVolume20 = avgLong(volumes, 20);

            boolean condMa = ma5 > ma20;
            boolean condSeason = todayClose > ma60;
            boolean condVolume = avgVolume20 > 0 && todayVolume > avgVolume20 * 1.5;
            boolean condKd = isKDCross(prices);
            boolean condMacd = isMacdPositive(closes);
            boolean condForeign = isForeignBuy(chips);

            double score = 0;
            if (condMa) score += 20;
            if (condSeason) score += 15;
            if (condVolume) score += 15;
            if (condKd) score += 15;
            if (condMacd) score += 15;
            if (condForeign) score += 20;

            List<String> triggers = new ArrayList<>();
            if (condMa) triggers.add("5MA > 20MA");
            if (condSeason) triggers.add("收盤站上60MA");
            if (condVolume) triggers.add("成交量 > 20日均量1.5倍");
            if (condKd) triggers.add("KD黃金交叉");
            if (condMacd) triggers.add("MACD翻正");
            if (condForeign) triggers.add("外資連買3天");

            BigDecimal foreignNetBuy = querySingleDecimal("""
                SELECT foreign_net_buy
                FROM daily_chip
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 1
            """, code);

            BigDecimal trustNetBuy = querySingleDecimal("""
                SELECT investment_trust_net_buy
                FROM daily_chip
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 1
            """, code);

            BigDecimal dealerNetBuy = querySingleDecimal("""
                SELECT dealer_net_buy
                FROM daily_chip
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 1
            """, code);

            BigDecimal peRatio = querySingleDecimal("""
                SELECT pe_ratio
                FROM daily_valuation
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 1
            """, code);

            BigDecimal eps = querySingleDecimal("""
                SELECT eps
                FROM daily_valuation
                WHERE stock_code = ?
                ORDER BY trade_date DESC
                LIMIT 1
            """, code);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("stockCode", code);
            row.put("stockName", name);
            row.put("tradeDate", latest.get("trade_date"));
            row.put("openPrice", latest.get("open_price"));
            row.put("highPrice", latest.get("high_price"));
            row.put("lowPrice", latest.get("low_price"));
            row.put("closePrice", latest.get("close_price"));
            row.put("volume", latest.get("volume"));
            row.put("turnoverAmount", latest.get("turnover_amount"));
            row.put("ma5", round(ma5));
            row.put("ma20", round(ma20));
            row.put("ma60", round(ma60));
            row.put("foreignNetBuy", foreignNetBuy);
            row.put("trustNetBuy", trustNetBuy);
            row.put("dealerNetBuy", dealerNetBuy);
            row.put("peRatio", peRatio);
            row.put("eps", eps);
            row.put("predictionScore", round(score));
            row.put("predictionProbability", round(score / 100.0 * 100.0));
            row.put("buySignalDate", latest.get("trade_date"));
            row.put("triggerConditions", String.join("、", triggers));
            row.put("bestEntryPrice", round(todayClose * 0.995));
            row.put("takeProfitPrice", round(todayClose * 1.05));
            row.put("stopLossPrice", round(todayClose * 0.97));
            row.put("backtestWinRate", null);

            result.add(row);
        }

        result.sort((a, b) -> Double.compare(
                getDouble(b.get("predictionScore")),
                getDouble(a.get("predictionScore"))
        ));

        return result.subList(0, Math.min(10, result.size()));
    }

    private BigDecimal querySingleDecimal(String sql, String code) {
        List<BigDecimal> list = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getBigDecimal(1),
                code
        );
        return list.isEmpty() ? null : list.get(0);
    }

    private List<Double> extractDouble(List<Map<String, Object>> list, String key) {
        List<Double> result = new ArrayList<>();
        for (Map<String, Object> row : list) {
            Object v = row.get(key);
            if (v != null) {
                result.add(((Number) v).doubleValue());
            }
        }
        return result;
    }

    private List<Long> extractLong(List<Map<String, Object>> list, String key) {
        List<Long> result = new ArrayList<>();
        for (Map<String, Object> row : list) {
            Object v = row.get(key);
            if (v != null) {
                result.add(((Number) v).longValue());
            }
        }
        return result;
    }

    private double avg(List<Double> list, int n) {
        return list.stream().limit(n).mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double avgLong(List<Long> list, int n) {
        return list.stream().limit(n).mapToDouble(Long::doubleValue).average().orElse(0);
    }

    private boolean isKDCross(List<Map<String, Object>> prices) {
        if (prices.size() < 10) {
            return false;
        }

        double todayClose = getDouble(prices.get(0).get("close_price"));
        double yesterdayClose = getDouble(prices.get(1).get("close_price"));

        return todayClose > yesterdayClose;
    }

    private boolean isMacdPositive(List<Double> closes) {
        if (closes.size() < 26) {
            return false;
        }

        double ema12 = ema(closes, 12);
        double ema26 = ema(closes, 26);
        return ema12 > ema26;
    }

    private double ema(List<Double> data, int period) {
        if (data.size() <= period) {
            return 0;
        }

        double k = 2.0 / (period + 1);
        double ema = data.get(period);

        for (int i = period - 1; i >= 0; i--) {
            ema = data.get(i) * k + ema * (1 - k);
        }
        return ema;
    }

    private boolean isForeignBuy(List<Map<String, Object>> chips) {
        if (chips.size() < 3) {
            return false;
        }

        for (Map<String, Object> chip : chips) {
            Object value = chip.get("foreign_net_buy");
            if (value == null || ((Number) value).longValue() <= 0) {
                return false;
            }
        }
        return true;
    }

    private double getDouble(Object value) {
        return value == null ? 0 : ((Number) value).doubleValue();
    }

    private long getLong(Object value) {
        return value == null ? 0 : ((Number) value).longValue();
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
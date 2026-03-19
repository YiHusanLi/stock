package tool.stock.service;

import tool.stock.dto.WarrantSearchRequest;
import tool.stock.dto.WarrantSearchResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WarrantScreenerService {

    private final JdbcTemplate jdbcTemplate;

    public WarrantScreenerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WarrantSearchResponse> search(WarrantSearchRequest request) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("""
            SELECT
                wb.warrant_code,
                wb.warrant_name,
                wb.underlying_code,
                wb.underlying_name,
                wb.warrant_type,
                wb.issuer_name,
                wb.strike_price,
                wb.maturity_date,
                wq.trade_date,
                wq.close_price AS warrant_close_price,
                wq.trade_volume,
                wq.bid_price,
                wq.ask_price,
                sq.close_price AS underlying_close_price
            FROM warrant_basic wb
            JOIN warrant_quote_daily wq
              ON wb.warrant_code = wq.warrant_code
            JOIN stock_quote_daily sq
              ON wb.underlying_code = sq.stock_code
             AND sq.trade_date = wq.trade_date
            WHERE 1 = 1
        """);

        if (request.getUnderlyingCode() != null && !request.getUnderlyingCode().isBlank()) {
            sql.append(" AND wb.underlying_code = ? ");
            params.add(request.getUnderlyingCode().trim());
        }

        if (request.getWarrantType() != null && !request.getWarrantType().isBlank()) {
            sql.append(" AND wb.warrant_type = ? ");
            params.add(request.getWarrantType().trim().toUpperCase());
        }

        if (request.getIssuerName() != null && !request.getIssuerName().isBlank()) {
            sql.append(" AND wb.issuer_name LIKE ? ");
            params.add("%" + request.getIssuerName().trim() + "%");
        }

        if (request.getMinPrice() != null) {
            sql.append(" AND wq.close_price >= ? ");
            params.add(request.getMinPrice());
        }

        if (request.getMaxPrice() != null) {
            sql.append(" AND wq.close_price <= ? ");
            params.add(request.getMaxPrice());
        }

        if (request.getMinTradeVolume() != null) {
            sql.append(" AND wq.trade_volume >= ? ");
            params.add(request.getMinTradeVolume());
        }

        sql.append(" ORDER BY wq.trade_date DESC, wq.trade_volume DESC ");

        List<WarrantSearchResponse> result = jdbcTemplate.query(sql.toString(), params.toArray(), (ResultSet rs, int rowNum) -> {
            LocalDate tradeDate = rs.getDate("trade_date").toLocalDate();
            LocalDate maturityDate = rs.getDate("maturity_date").toLocalDate();

            BigDecimal underlyingClose = rs.getBigDecimal("underlying_close_price");
            BigDecimal warrantClose = rs.getBigDecimal("warrant_close_price");
            BigDecimal strikePrice = rs.getBigDecimal("strike_price");
            BigDecimal bidPrice = rs.getBigDecimal("bid_price");
            BigDecimal askPrice = rs.getBigDecimal("ask_price");

            String warrantType = rs.getString("warrant_type");

            int daysToMaturity = (int) ChronoUnit.DAYS.between(tradeDate, maturityDate);

            BigDecimal moneynessPercent = calculateMoneynessPercent(warrantType, underlyingClose, strikePrice);
            BigDecimal spreadPercent = calculateSpreadPercent(bidPrice, askPrice);
            BigDecimal score = calculateScore(daysToMaturity, rs.getLong("trade_volume"), spreadPercent, moneynessPercent);

            WarrantSearchResponse dto = new WarrantSearchResponse();
            dto.setWarrantCode(rs.getString("warrant_code"));
            dto.setWarrantName(rs.getString("warrant_name"));
            dto.setUnderlyingCode(rs.getString("underlying_code"));
            dto.setUnderlyingName(rs.getString("underlying_name"));
            dto.setWarrantType(warrantType);
            dto.setIssuerName(rs.getString("issuer_name"));
            dto.setUnderlyingClosePrice(underlyingClose);
            dto.setWarrantClosePrice(warrantClose);
            dto.setStrikePrice(strikePrice);
            dto.setDaysToMaturity(daysToMaturity);
            dto.setMoneynessPercent(moneynessPercent);
            dto.setTradeVolume(rs.getLong("trade_volume"));
            dto.setBidPrice(bidPrice);
            dto.setAskPrice(askPrice);
            dto.setSpreadPercent(spreadPercent);
            dto.setScore(score);
            dto.setTradeDate(tradeDate);
            dto.setMaturityDate(maturityDate);
            return dto;
        });

        return result.stream()
                .filter(x -> request.getMinDaysToMaturity() == null || x.getDaysToMaturity() >= request.getMinDaysToMaturity())
                .filter(x -> request.getMaxDaysToMaturity() == null || x.getDaysToMaturity() <= request.getMaxDaysToMaturity())
                .filter(x -> request.getMinMoneynessPercent() == null || x.getMoneynessPercent().compareTo(request.getMinMoneynessPercent()) >= 0)
                .filter(x -> request.getMaxMoneynessPercent() == null || x.getMoneynessPercent().compareTo(request.getMaxMoneynessPercent()) <= 0)
                .sorted((a, b) -> sort(a, b, request))
                .toList();
    }

    private BigDecimal calculateMoneynessPercent(String warrantType, BigDecimal underlyingClose, BigDecimal strikePrice) {
        if (underlyingClose == null || strikePrice == null || strikePrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal result;
        if ("PUT".equalsIgnoreCase(warrantType)) {
            result = strikePrice.subtract(underlyingClose)
                    .divide(strikePrice, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            result = underlyingClose.subtract(strikePrice)
                    .divide(strikePrice, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSpreadPercent(BigDecimal bidPrice, BigDecimal askPrice) {
        if (bidPrice == null || askPrice == null) {
            return BigDecimal.valueOf(999);
        }

        BigDecimal sum = bidPrice.add(askPrice);
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(999);
        }

        BigDecimal avg = sum.divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
        return askPrice.subtract(bidPrice)
                .divide(avg, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateScore(int daysToMaturity, long volume, BigDecimal spreadPercent, BigDecimal moneynessPercent) {
        BigDecimal score = BigDecimal.ZERO;

        if (daysToMaturity >= 30 && daysToMaturity <= 90) {
            score = score.add(BigDecimal.valueOf(30));
        } else if (daysToMaturity > 0) {
            score = score.add(BigDecimal.valueOf(10));
        }

        if (volume >= 1000_000) {
            score = score.add(BigDecimal.valueOf(30));
        } else if (volume >= 100_000) {
            score = score.add(BigDecimal.valueOf(20));
        } else if (volume >= 10_000) {
            score = score.add(BigDecimal.valueOf(10));
        }

        if (spreadPercent.compareTo(BigDecimal.valueOf(1.5)) <= 0) {
            score = score.add(BigDecimal.valueOf(25));
        } else if (spreadPercent.compareTo(BigDecimal.valueOf(3)) <= 0) {
            score = score.add(BigDecimal.valueOf(15));
        }

        BigDecimal absMoneyness = moneynessPercent.abs();
        if (absMoneyness.compareTo(BigDecimal.valueOf(5)) <= 0) {
            score = score.add(BigDecimal.valueOf(15));
        } else if (absMoneyness.compareTo(BigDecimal.valueOf(10)) <= 0) {
            score = score.add(BigDecimal.valueOf(8));
        }

        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private int sort(WarrantSearchResponse a, WarrantSearchResponse b, WarrantSearchRequest request) {
        String sortBy = request.getSortBy() == null ? "score" : request.getSortBy().toLowerCase();
        String sortOrder = request.getSortOrder() == null ? "desc" : request.getSortOrder().toLowerCase();

        int result;
        switch (sortBy) {
            case "volume":
                result = Long.compare(a.getTradeVolume(), b.getTradeVolume());
                break;
            case "spread":
                result = a.getSpreadPercent().compareTo(b.getSpreadPercent());
                break;
            case "days":
                result = Integer.compare(a.getDaysToMaturity(), b.getDaysToMaturity());
                break;
            default:
                result = a.getScore().compareTo(b.getScore());
                break;
        }

        return "asc".equals(sortOrder) ? result : -result;
    }
}
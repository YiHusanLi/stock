package tool.stock.v8.service;

import org.springframework.stereotype.Service;
import tool.stock.v8.dto.TrendlineResult;
import tool.stock.v8.entity.StockPriceDaily;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 計算最近 10 日高點的線性回歸趨勢線。
 */
@Service
public class TrendlineService {

    public TrendlineResult calculateTrendline(List<StockPriceDaily> history, LocalDate tradeDate, BigDecimal todayClosePrice) {
        List<StockPriceDaily> window = history.stream()
                .filter(item -> !item.getTradeDate().isAfter(tradeDate))
                .sorted(Comparator.comparing(StockPriceDaily::getTradeDate))
                .collect(Collectors.toList());
        if (window.size() < 10) {
            return new TrendlineResult(BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP), false, false);
        }

        List<StockPriceDaily> last10 = window.subList(window.size() - 10, window.size());
        int n = last10.size();
        BigDecimal sumX = BigDecimal.ZERO;
        BigDecimal sumY = BigDecimal.ZERO;
        BigDecimal sumXY = BigDecimal.ZERO;
        BigDecimal sumX2 = BigDecimal.ZERO;

        for (int i = 0; i < n; i++) {
            BigDecimal x = BigDecimal.valueOf(i + 1L);
            BigDecimal y = last10.get(i).getHighPrice();
            sumX = sumX.add(x);
            sumY = sumY.add(y);
            sumXY = sumXY.add(x.multiply(y));
            sumX2 = sumX2.add(x.multiply(x));
        }

        BigDecimal nValue = BigDecimal.valueOf(n);
        BigDecimal numerator = nValue.multiply(sumXY).subtract(sumX.multiply(sumY));
        BigDecimal denominator = nValue.multiply(sumX2).subtract(sumX.multiply(sumX));
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return new TrendlineResult(BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP), false, false);
        }

        BigDecimal slope = numerator.divide(denominator, 6, RoundingMode.HALF_UP);
        BigDecimal intercept = sumY.subtract(slope.multiply(sumX))
                .divide(nValue, 6, RoundingMode.HALF_UP);
        BigDecimal todayTrendlineValue = intercept.add(slope.multiply(BigDecimal.valueOf(n))).setScale(4, RoundingMode.HALF_UP);
        boolean descendingTrend = slope.compareTo(BigDecimal.ZERO) < 0;
        boolean breakout = descendingTrend && todayClosePrice.compareTo(todayTrendlineValue) > 0;
        return new TrendlineResult(slope, todayTrendlineValue, descendingTrend, breakout);
    }
}

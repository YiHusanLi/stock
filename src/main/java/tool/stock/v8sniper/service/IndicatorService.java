package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.TrendBreakResult;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;
import tool.stock.v8sniper.entity.StockIndicatorDailyEntity;
import tool.stock.v8sniper.repository.StockDailyQuoteRepository;
import tool.stock.v8sniper.repository.StockIndicatorDailyRepository;
import tool.stock.v8sniper.util.MathUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndicatorService {
    private final StockDailyQuoteRepository quoteRepository;
    private final StockIndicatorDailyRepository indicatorRepository;
    private final V8Properties properties;
    public IndicatorService(StockDailyQuoteRepository quoteRepository, StockIndicatorDailyRepository indicatorRepository, V8Properties properties) {
        this.quoteRepository = quoteRepository; this.indicatorRepository = indicatorRepository; this.properties = properties;
    }

    @Transactional
    public int rebuild(LocalDate tradeDate) {
        List<StockDailyQuoteEntity> all = quoteRepository.findAll().stream().sorted(Comparator.comparing(StockDailyQuoteEntity::getTradeDate)).collect(Collectors.toList());
        Map<String, List<StockDailyQuoteEntity>> grouped = all.stream().collect(Collectors.groupingBy(StockDailyQuoteEntity::getStockCode));
        int count = 0;
        for (List<StockDailyQuoteEntity> history : grouped.values()) {
            for (int i = 0; i < history.size(); i++) {
                StockDailyQuoteEntity current = history.get(i);
                if (!tradeDate.equals(current.getTradeDate())) continue;
                List<StockDailyQuoteEntity> sub = history.subList(0, i + 1);
                StockIndicatorDailyEntity entity = indicatorRepository.findByTradeDateAndStockCode(tradeDate, current.getStockCode()).orElseGet(StockIndicatorDailyEntity::new);
                entity.setTradeDate(tradeDate); entity.setStockCode(current.getStockCode());
                entity.setMa5(avgClose(sub, 5)); entity.setMa10(avgClose(sub, 10)); entity.setMa20(avgClose(sub, 20)); entity.setMa60(avgClose(sub, 60));
                entity.setAvgVolume5(avgVolume(sub, 5)); entity.setAvgVolume10(avgVolume(sub, 10)); entity.setAvgVolume20(avgVolume(sub, 20));
                entity.setVolumeRatio(current.getVolume() == null || entity.getAvgVolume5().compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ZERO : BigDecimal.valueOf(current.getVolume()).divide(entity.getAvgVolume5(), 4, RoundingMode.HALF_UP));
                TrendBreakResult trend = evaluateTrend(sub, current);
                entity.setTrendSlope(trend.getSlope()); entity.setTrendPressure(trend.getPressure());
                indicatorRepository.save(entity); count++;
            }
        }
        return count;
    }

    public TrendBreakResult evaluateTrend(List<StockDailyQuoteEntity> history, StockDailyQuoteEntity today) {
        int lookback = properties.getStrategy().getV8().getTrend().getLookbackDays();
        List<StockDailyQuoteEntity> window = history.stream().filter(x -> !x.getTradeDate().isAfter(today.getTradeDate())).collect(Collectors.toList());
        if (window.size() < Math.max(5, lookback)) {
            return new TrendBreakResult(false, BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP), "資料不足", "至少" + lookback + "日", "資料不足，無法判斷趨勢");
        }
        List<StockDailyQuoteEntity> recent = window.subList(window.size() - lookback, window.size());
        if ("SIMPLE".equalsIgnoreCase(properties.getStrategy().getV8().getTrend().getMode())) {
            BigDecimal highest = recent.subList(0, recent.size() - 1).stream().map(StockDailyQuoteEntity::getHighPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal first = recent.get(0).getHighPrice();
            BigDecimal lastBeforeToday = recent.get(recent.size() - 2).getHighPrice();
            boolean descending = first.compareTo(lastBeforeToday) > 0;
            boolean breakout = descending && today.getClosePrice().compareTo(highest) > 0;
            BigDecimal slope = lastBeforeToday.subtract(first).divide(BigDecimal.valueOf(recent.size() - 1L), 6, RoundingMode.HALF_UP);
            return new TrendBreakResult(breakout, slope, highest, "close=" + today.getClosePrice(), "pressure=" + highest, breakout ? "簡化下降趨勢突破成立" : "簡化下降趨勢突破未成立");
        }
        BigDecimal sumX = BigDecimal.ZERO, sumY = BigDecimal.ZERO, sumXY = BigDecimal.ZERO, sumX2 = BigDecimal.ZERO;
        for (int i = 0; i < recent.size(); i++) {
            BigDecimal x = BigDecimal.valueOf(i + 1L); BigDecimal y = recent.get(i).getHighPrice();
            sumX = sumX.add(x); sumY = sumY.add(y); sumXY = sumXY.add(x.multiply(y)); sumX2 = sumX2.add(x.multiply(x));
        }
        BigDecimal n = BigDecimal.valueOf(recent.size());
        BigDecimal denominator = n.multiply(sumX2).subtract(sumX.multiply(sumX));
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return new TrendBreakResult(false, BigDecimal.ZERO, BigDecimal.ZERO, "迴歸分母為0", "無", "線性回歸失敗");
        }
        BigDecimal slope = n.multiply(sumXY).subtract(sumX.multiply(sumY)).divide(denominator, 6, RoundingMode.HALF_UP);
        BigDecimal intercept = sumY.subtract(slope.multiply(sumX)).divide(n, 6, RoundingMode.HALF_UP);
        BigDecimal pressure = intercept.add(slope.multiply(BigDecimal.valueOf(recent.size()))).setScale(4, RoundingMode.HALF_UP);
        boolean breakout = slope.compareTo(BigDecimal.ZERO) < 0 && today.getClosePrice().compareTo(pressure) > 0;
        return new TrendBreakResult(breakout, slope, pressure, "close=" + today.getClosePrice(), "pressure=" + pressure, breakout ? "回歸下降趨勢突破成立" : "回歸下降趨勢突破未成立");
    }

    public int trendScore() { return properties.getStrategy().getV8().getScore().getWeightTrendBreakout(); }
    private BigDecimal avgClose(List<StockDailyQuoteEntity> history, int days) { return MathUtil.average(history.subList(Math.max(0, history.size()-days), history.size()).stream().map(StockDailyQuoteEntity::getClosePrice).collect(Collectors.toList()), 4); }
    private BigDecimal avgVolume(List<StockDailyQuoteEntity> history, int days) {
        List<BigDecimal> volumes = new ArrayList<>();
        for (StockDailyQuoteEntity entity : history.subList(Math.max(0, history.size()-days), history.size())) { volumes.add(BigDecimal.valueOf(entity.getVolume() == null ? 0L : entity.getVolume())); }
        return MathUtil.average(volumes, 4);
    }
}

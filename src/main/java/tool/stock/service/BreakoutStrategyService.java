package tool.stock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tool.stock.dto.StrategyEvaluationResult;
import tool.stock.dto.TrendlineResult;
import tool.stock.entity.StockIndicatorDaily;
import tool.stock.entity.StockPriceDaily;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * V8_BREAKOUT 規則判斷。
 */
@Service
public class BreakoutStrategyService {

    private final TrendlineService trendlineService;

    @Value("${app.strategy.code}")
    private String strategyCode;

    public BreakoutStrategyService(TrendlineService trendlineService) {
        this.trendlineService = trendlineService;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public StrategyEvaluationResult evaluate(StockPriceDaily todayPrice,
                                             StockIndicatorDaily indicator,
                                             List<StockPriceDaily> history,
                                             LocalDate tradeDate) {
        List<String> remarks = new ArrayList<>();

        boolean basePrice = todayPrice.getClosePrice().compareTo(BigDecimal.TEN) >= 0;
        boolean baseVolume = todayPrice.getVolume() >= 500_000L;
        boolean basePct = indicator.getPctChange().compareTo(BigDecimal.valueOf(4.0)) <= 0;
        if (!basePrice) remarks.add("股價小於10");
        if (!baseVolume) remarks.add("成交量小於500000");
        if (!basePct) remarks.add("漲幅超過4%");

        BigDecimal prev20AvgVolume = calcPrevious20AvgVolume(history, tradeDate);
        boolean squeezeBurst = indicator.getAvgVol5().compareTo(prev20AvgVolume.multiply(BigDecimal.valueOf(0.8))) < 0
                && BigDecimal.valueOf(todayPrice.getVolume()).compareTo(prev20AvgVolume.multiply(BigDecimal.valueOf(1.8))) > 0;
        if (!squeezeBurst) remarks.add("未符合量縮後爆量");

        boolean multiMaBreak = todayPrice.getClosePrice().compareTo(indicator.getMa5()) > 0
                && todayPrice.getClosePrice().compareTo(indicator.getMa10()) > 0
                && todayPrice.getClosePrice().compareTo(indicator.getMa20()) > 0;
        if (!multiMaBreak) remarks.add("未站上MA5/MA10/MA20");

        TrendlineResult trendlineResult = trendlineService.calculateTrendline(history, tradeDate, todayPrice.getClosePrice());
        boolean trendBreak = trendlineResult.isBreakout();
        if (!trendBreak) remarks.add("未突破下降趨勢");

        boolean matched = basePrice && baseVolume && basePct && squeezeBurst && multiMaBreak && trendBreak;
        if (matched) {
            remarks.clear();
            remarks.add("符合 V8_BREAKOUT");
        }

        return new StrategyEvaluationResult(
                matched,
                squeezeBurst,
                multiMaBreak,
                trendBreak,
                trendlineResult.getSlope().setScale(6, RoundingMode.HALF_UP),
                trendlineResult.getTodayTrendlineValue().setScale(4, RoundingMode.HALF_UP),
                String.join("；", remarks)
        );
    }

    private BigDecimal calcPrevious20AvgVolume(List<StockPriceDaily> history, LocalDate tradeDate) {
        List<StockPriceDaily> eligible = history.stream()
                .filter(item -> item.getTradeDate().isBefore(tradeDate))
                .toList();
        if (eligible.isEmpty()) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
        int fromIndex = Math.max(0, eligible.size() - 20);
        List<StockPriceDaily> previous20 = eligible.subList(fromIndex, eligible.size());
        BigDecimal sum = BigDecimal.ZERO;
        for (StockPriceDaily price : previous20) {
            sum = sum.add(BigDecimal.valueOf(price.getVolume()));
        }
        return sum.divide(BigDecimal.valueOf(previous20.size()), 4, RoundingMode.HALF_UP);
    }
}

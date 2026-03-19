package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.dto.TrendBreakResult;
import tool.stock.v8sniper.service.IndicatorService;
import tool.stock.v8sniper.strategy.StrategyRule;

@Component
public class DownTrendBreakoutRule implements StrategyRule {
    private final IndicatorService indicatorService;
    public DownTrendBreakoutRule(IndicatorService indicatorService) { this.indicatorService = indicatorService; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        TrendBreakResult result = indicatorService.evaluateTrend(snapshot.getHistory(), snapshot.getToday());
        return new RuleEvaluation("TREND_BREAKOUT", "突破下降趨勢", result.isPassed(),
                result.isPassed() ? indicatorService.trendScore() : 0,
                result.getValue(), result.getThreshold(), result.getReason());
    }
}

package tool.stock.v8sniper.strategy;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.dto.StrategyResultBundle;
import tool.stock.v8sniper.entity.StrategyPickDetailEntity;
import tool.stock.v8sniper.entity.StrategyPickResultEntity;
import tool.stock.v8sniper.rules.DownTrendBreakoutRule;
import tool.stock.v8sniper.rules.MaBreakoutRule;
import tool.stock.v8sniper.rules.PctChangeFilterRule;
import tool.stock.v8sniper.rules.PriceFilterRule;
import tool.stock.v8sniper.rules.VolumeBreakoutRule;
import tool.stock.v8sniper.rules.VolumeFilterRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class V8SniperStrategy {
    private final V8Properties properties;
    private final PriceFilterRule priceFilterRule;
    private final VolumeFilterRule volumeFilterRule;
    private final PctChangeFilterRule pctChangeFilterRule;
    private final VolumeBreakoutRule volumeBreakoutRule;
    private final MaBreakoutRule maBreakoutRule;
    private final DownTrendBreakoutRule downTrendBreakoutRule;

    public V8SniperStrategy(V8Properties properties, PriceFilterRule priceFilterRule, VolumeFilterRule volumeFilterRule,
                            PctChangeFilterRule pctChangeFilterRule, VolumeBreakoutRule volumeBreakoutRule,
                            MaBreakoutRule maBreakoutRule, DownTrendBreakoutRule downTrendBreakoutRule) {
        this.properties = properties; this.priceFilterRule = priceFilterRule; this.volumeFilterRule = volumeFilterRule;
        this.pctChangeFilterRule = pctChangeFilterRule; this.volumeBreakoutRule = volumeBreakoutRule;
        this.maBreakoutRule = maBreakoutRule; this.downTrendBreakoutRule = downTrendBreakoutRule;
    }

    public StrategyResultBundle evaluate(StockSnapshot snapshot) {
        List<RuleEvaluation> evaluations = List.of(
                priceFilterRule.evaluate(snapshot),
                volumeFilterRule.evaluate(snapshot),
                pctChangeFilterRule.evaluate(snapshot),
                volumeBreakoutRule.evaluate(snapshot),
                maBreakoutRule.evaluate(snapshot),
                downTrendBreakoutRule.evaluate(snapshot)
        );
        boolean basePassed = evaluations.stream().filter(e -> e.getRuleCode().endsWith("FILTER")).allMatch(RuleEvaluation::isPassed);
        int totalScore = evaluations.stream().mapToInt(RuleEvaluation::getScore).sum();
        boolean picked = basePassed && totalScore >= 60
                && evaluations.stream().anyMatch(e -> "VOL_BREAKOUT".equals(e.getRuleCode()) && e.isPassed())
                && evaluations.stream().anyMatch(e -> "MA_BREAKOUT".equals(e.getRuleCode()) && e.isPassed())
                && evaluations.stream().anyMatch(e -> "TREND_BREAKOUT".equals(e.getRuleCode()) && e.isPassed());

        StrategyPickResultEntity result = new StrategyPickResultEntity();
        result.setTradeDate(snapshot.getToday().getTradeDate());
        result.setStrategyName(properties.getStrategy().getV8().getStrategyName());
        result.setStockCode(snapshot.getToday().getStockCode());
        result.setStockName(snapshot.getBasic() == null ? null : snapshot.getBasic().getStockName());
        result.setIndustry(snapshot.getBasic() == null ? "未分類" : snapshot.getBasic().getIndustry());
        result.setTotalScore(totalScore);
        result.setClosePrice(snapshot.getToday().getClosePrice());
        result.setPctChange(snapshot.getToday().getPctChange());
        result.setVolume(snapshot.getToday().getVolume());
        result.setVolumeRatio(snapshot.getIndicator().getVolumeRatio());
        result.setTrendSlope(snapshot.getIndicator().getTrendSlope());
        result.setSuggestedBuyPrice(snapshot.getToday().getClosePrice().multiply(properties.getStrategy().getV8().getBuyPrice().getDiscountRate()).setScale(4, RoundingMode.HALF_UP));
        result.setPickReason(evaluations.stream().filter(RuleEvaluation::isPassed).map(RuleEvaluation::getRuleName).collect(Collectors.joining(" + ")));
        result.setRating(resolveRating(totalScore));
        result.setLimitUp(snapshot.getToday().getPctChange() != null && snapshot.getToday().getPctChange().compareTo(BigDecimal.valueOf(9.5)) >= 0);
        result.setPreviousDayPicked(snapshot.isPreviousDayPicked());

        List<StrategyPickDetailEntity> details = new ArrayList<>();
        for (RuleEvaluation evaluation : evaluations) {
            StrategyPickDetailEntity detail = new StrategyPickDetailEntity();
            detail.setTradeDate(snapshot.getToday().getTradeDate());
            detail.setStockCode(snapshot.getToday().getStockCode());
            detail.setStrategyName(properties.getStrategy().getV8().getStrategyName());
            detail.setRuleCode(evaluation.getRuleCode());
            detail.setRuleName(evaluation.getRuleName());
            detail.setScore(evaluation.getScore());
            detail.setPassed(evaluation.isPassed());
            detail.setRuleValue(evaluation.getRuleValue());
            detail.setRuleThreshold(evaluation.getThreshold());
            details.add(detail);
        }
        return new StrategyResultBundle(picked, result, details);
    }

    private String resolveRating(int score) {
        if (score >= 85) return "親股雞形";
        if (score >= 70) return "強勢突破";
        return "觀察名單";
    }
}

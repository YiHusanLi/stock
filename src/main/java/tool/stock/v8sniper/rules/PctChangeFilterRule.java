package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.strategy.StrategyRule;

@Component
public class PctChangeFilterRule implements StrategyRule {
    private final V8Properties properties;
    public PctChangeFilterRule(V8Properties properties) { this.properties = properties; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        boolean passed = snapshot.getToday().getPctChange() != null && snapshot.getToday().getPctChange().compareTo(properties.getStrategy().getV8().getMinPctChange()) >= 0;
        int score = passed ? properties.getStrategy().getV8().getScore().getWeightPctStrength() : 0;
        return new RuleEvaluation("PCT_FILTER", "漲幅強度", passed, score,
                String.valueOf(snapshot.getToday().getPctChange()), properties.getStrategy().getV8().getMinPctChange().toPlainString(),
                passed ? "漲幅達標" : "漲幅不足");
    }
}

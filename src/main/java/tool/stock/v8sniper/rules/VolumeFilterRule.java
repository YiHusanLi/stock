package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.strategy.StrategyRule;

@Component
public class VolumeFilterRule implements StrategyRule {
    private final V8Properties properties;
    public VolumeFilterRule(V8Properties properties) { this.properties = properties; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        boolean passed = snapshot.getToday().getVolume() != null && snapshot.getToday().getVolume() >= properties.getStrategy().getV8().getMinVolume();
        int score = passed ? properties.getStrategy().getV8().getScore().getWeightVolumeThreshold() : 0;
        return new RuleEvaluation("VOLUME_FILTER", "成交量門檻", passed, score,
                String.valueOf(snapshot.getToday().getVolume()), String.valueOf(properties.getStrategy().getV8().getMinVolume()),
                passed ? "成交量達標" : "成交量不足");
    }
}

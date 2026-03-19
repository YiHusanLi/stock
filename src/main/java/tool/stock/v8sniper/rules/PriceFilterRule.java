package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.strategy.StrategyRule;

@Component
public class PriceFilterRule implements StrategyRule {
    private final V8Properties properties;
    public PriceFilterRule(V8Properties properties) { this.properties = properties; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        boolean passed = snapshot.getToday().getClosePrice().compareTo(properties.getStrategy().getV8().getMinPrice()) >= 0;
        return new RuleEvaluation("PRICE_FILTER", "股價門檻", passed, 0,
                snapshot.getToday().getClosePrice().toPlainString(), properties.getStrategy().getV8().getMinPrice().toPlainString(),
                passed ? "股價符合門檻" : "股價未達門檻");
    }
}

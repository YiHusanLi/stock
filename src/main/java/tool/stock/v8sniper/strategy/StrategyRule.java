package tool.stock.v8sniper.strategy;

import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;

public interface StrategyRule {
    RuleEvaluation evaluate(StockSnapshot snapshot);
}

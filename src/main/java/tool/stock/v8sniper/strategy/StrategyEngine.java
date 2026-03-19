package tool.stock.v8sniper.strategy;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.dto.StrategyResultBundle;
import tool.stock.v8sniper.dto.StockSnapshot;

@Component
public class StrategyEngine {
    private final V8SniperStrategy v8SniperStrategy;
    public StrategyEngine(V8SniperStrategy v8SniperStrategy) { this.v8SniperStrategy = v8SniperStrategy; }
    public StrategyResultBundle runV8(StockSnapshot snapshot) { return v8SniperStrategy.evaluate(snapshot); }
}

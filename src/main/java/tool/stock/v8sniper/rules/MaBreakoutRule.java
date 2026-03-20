package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;
import tool.stock.v8sniper.strategy.StrategyRule;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MaBreakoutRule implements StrategyRule {
    private final V8Properties properties;
    public MaBreakoutRule(V8Properties properties) { this.properties = properties; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        String mode = properties.getStrategy().getV8().getMa().getMode();
        boolean passed;
        if ("MA_CROSS_MULTI".equalsIgnoreCase(mode)) {
            List<StockDailyQuoteEntity> history = snapshot.getHistory();
            passed = history.size() >= 2
                    && snapshot.getIndicator().getMa5().compareTo(snapshot.getIndicator().getMa10()) > 0
                    && snapshot.getIndicator().getMa5().compareTo(snapshot.getIndicator().getMa20()) > 0
                    && snapshot.getToday().getClosePrice().compareTo(snapshot.getIndicator().getMa5()) >= 0;
        } else {
            passed = snapshot.getToday().getClosePrice().compareTo(snapshot.getIndicator().getMa5()) >= 0
                    && snapshot.getToday().getClosePrice().compareTo(snapshot.getIndicator().getMa10()) >= 0
                    && snapshot.getToday().getClosePrice().compareTo(snapshot.getIndicator().getMa20()) >= 0;
        }
        int score = passed ? properties.getStrategy().getV8().getScore().getWeightMaBreakout() : 0;
        return new RuleEvaluation("MA_BREAKOUT", "一線穿三線", passed, score,
                "close=" + snapshot.getToday().getClosePrice() + ",ma5=" + snapshot.getIndicator().getMa5() + ",ma10=" + snapshot.getIndicator().getMa10() + ",ma20=" + snapshot.getIndicator().getMa20(),
                mode,
                passed ? "均線突破成立" : "均線突破未成立");
    }
}

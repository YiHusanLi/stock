package tool.stock.v8sniper.rules;

import org.springframework.stereotype.Component;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RuleEvaluation;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.strategy.StrategyRule;

import java.math.BigDecimal;

@Component
public class VolumeBreakoutRule implements StrategyRule {
    private final V8Properties properties;
    public VolumeBreakoutRule(V8Properties properties) { this.properties = properties; }
    public RuleEvaluation evaluate(StockSnapshot snapshot) {
        BigDecimal avg5 = snapshot.getIndicator().getAvgVolume5();
        BigDecimal avg10 = snapshot.getIndicator().getAvgVolume10();
        BigDecimal todayVol = BigDecimal.valueOf(snapshot.getToday().getVolume() == null ? 0L : snapshot.getToday().getVolume());
        BigDecimal volumeRatio = snapshot.getIndicator().getVolumeRatio();
        boolean shrink = avg5.compareTo(avg10) < 0;
        boolean passed = shrink
                && todayVol.compareTo(avg5.multiply(properties.getStrategy().getV8().getVolume().getBreakoutMultiplier5())) >= 0
                && todayVol.compareTo(avg10.multiply(properties.getStrategy().getV8().getVolume().getBreakoutMultiplier10())) >= 0;
        int score = passed ? properties.getStrategy().getV8().getScore().getWeightVolumeBreakout() : 0;
        return new RuleEvaluation("VOL_BREAKOUT", "量縮後爆量", passed, score,
                "今日量=" + todayVol.toPlainString() + ",量比=" + volumeRatio,
                "avg5*" + properties.getStrategy().getV8().getVolume().getBreakoutMultiplier5() + " 且 avg10*" + properties.getStrategy().getV8().getVolume().getBreakoutMultiplier10(),
                passed ? "量縮後爆量成立" : "量縮或爆量條件不足");
    }
}

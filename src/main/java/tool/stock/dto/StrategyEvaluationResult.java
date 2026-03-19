package tool.stock.dto;

import java.math.BigDecimal;

/**
 * 策略判斷結果。
 */
public class StrategyEvaluationResult {

    private final boolean matched;
    private final boolean squeezeBurst;
    private final boolean multiMaBreak;
    private final boolean trendBreak;
    private final BigDecimal trendSlope;
    private final BigDecimal trendlineValue;
    private final String remark;

    public StrategyEvaluationResult(boolean matched,
                                    boolean squeezeBurst,
                                    boolean multiMaBreak,
                                    boolean trendBreak,
                                    BigDecimal trendSlope,
                                    BigDecimal trendlineValue,
                                    String remark) {
        this.matched = matched;
        this.squeezeBurst = squeezeBurst;
        this.multiMaBreak = multiMaBreak;
        this.trendBreak = trendBreak;
        this.trendSlope = trendSlope;
        this.trendlineValue = trendlineValue;
        this.remark = remark;
    }

    public boolean isMatched() { return matched; }
    public boolean isSqueezeBurst() { return squeezeBurst; }
    public boolean isMultiMaBreak() { return multiMaBreak; }
    public boolean isTrendBreak() { return trendBreak; }
    public BigDecimal getTrendSlope() { return trendSlope; }
    public BigDecimal getTrendlineValue() { return trendlineValue; }
    public String getRemark() { return remark; }
}

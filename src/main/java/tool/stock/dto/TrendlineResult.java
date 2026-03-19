package tool.stock.dto;

import java.math.BigDecimal;

/**
 * 趨勢線計算結果。
 */
public class TrendlineResult {

    private final BigDecimal slope;
    private final BigDecimal todayTrendlineValue;
    private final boolean descendingTrend;
    private final boolean breakout;

    public TrendlineResult(BigDecimal slope, BigDecimal todayTrendlineValue, boolean descendingTrend, boolean breakout) {
        this.slope = slope;
        this.todayTrendlineValue = todayTrendlineValue;
        this.descendingTrend = descendingTrend;
        this.breakout = breakout;
    }

    public BigDecimal getSlope() { return slope; }
    public BigDecimal getTodayTrendlineValue() { return todayTrendlineValue; }
    public boolean isDescendingTrend() { return descendingTrend; }
    public boolean isBreakout() { return breakout; }
}

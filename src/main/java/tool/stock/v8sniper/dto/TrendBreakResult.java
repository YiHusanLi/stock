package tool.stock.v8sniper.dto;

import java.math.BigDecimal;

public class TrendBreakResult {
    private final boolean passed;
    private final BigDecimal slope;
    private final BigDecimal pressure;
    private final String value;
    private final String threshold;
    private final String reason;
    public TrendBreakResult(boolean passed, BigDecimal slope, BigDecimal pressure, String value, String threshold, String reason) {
        this.passed = passed; this.slope = slope; this.pressure = pressure; this.value = value; this.threshold = threshold; this.reason = reason;
    }
    public boolean isPassed() { return passed; }
    public BigDecimal getSlope() { return slope; }
    public BigDecimal getPressure() { return pressure; }
    public String getValue() { return value; }
    public String getThreshold() { return threshold; }
    public String getReason() { return reason; }
}

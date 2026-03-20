package tool.stock.v8sniper.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class MathUtil {
    private MathUtil() {}

    public static BigDecimal average(List<BigDecimal> values, int scale) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
                count++;
            }
        }
        if (count == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        return sum.divide(BigDecimal.valueOf(count), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal pctChange(BigDecimal current, BigDecimal previous, int scale) {
        if (current == null || previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        return current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, scale, RoundingMode.HALF_UP);
    }
}

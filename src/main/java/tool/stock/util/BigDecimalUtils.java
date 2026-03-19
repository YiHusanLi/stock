package tool.stock.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * BigDecimal 工具。
 */
public final class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static BigDecimal average(List<BigDecimal> values, int scale) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(values.size()), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal percentageChange(BigDecimal current, BigDecimal previous, int scale) {
        if (current == null || previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        return current.subtract(previous)
                .divide(previous, scale + 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal ratioReturn(BigDecimal sellPrice, BigDecimal entryPrice, int scale) {
        if (sellPrice == null || entryPrice == null || entryPrice.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return sellPrice.divide(entryPrice, scale + 4, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .setScale(scale, RoundingMode.HALF_UP);
    }
}

package tool.stock.util;

import java.math.BigDecimal;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static Long toLong(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        try {
            return Long.parseLong(normalized);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        try {
            return new BigDecimal(normalized);
        } catch (Exception e) {
            return null;
        }
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim();
        if (v.isEmpty() || "--".equals(v) || "N/A".equalsIgnoreCase(v)) {
            return null;
        }
        return v.replace(",", "").replace("%", "");
    }
}
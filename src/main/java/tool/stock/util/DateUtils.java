package tool.stock.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    public static LocalDate parseTwDate(String value) {
        if (value == null || value.trim().isEmpty() || "--".equals(value.trim())) {
            return null;
        }

        String v = value.trim();

        try {
            if (v.matches("\\d{8}")) {
                return LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
            if (v.matches("\\d{4}/\\d{2}/\\d{2}")) {
                return LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            }
            if (v.matches("\\d{3}/\\d{2}/\\d{2}")) {
                String[] parts = v.split("/");
                return LocalDate.of(
                        Integer.parseInt(parts[0]) + 1911,
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                );
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}
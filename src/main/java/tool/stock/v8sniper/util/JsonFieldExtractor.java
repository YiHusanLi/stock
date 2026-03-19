package tool.stock.v8sniper.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

/**
 * TWSE 常見欄位清洗工具。
 */
public final class JsonFieldExtractor {
    private JsonFieldExtractor() {}

    public static String text(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode child = node.get(key);
            if (child != null && !child.isNull()) {
                String value = child.asText();
                if (value != null) {
                    value = value.trim();
                    if (!value.isEmpty() && !"--".equals(value) && !"-".equals(value)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    public static BigDecimal decimal(JsonNode node, String... keys) {
        String text = text(node, keys);
        if (text == null) {
            return null;
        }
        String normalized = text.replace(",", "").replace("%", "").replace("+", "").replace("X", "").trim();
        if (normalized.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(normalized);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Long longValue(JsonNode node, String... keys) {
        BigDecimal decimal = decimal(node, keys);
        return decimal == null ? null : decimal.longValue();
    }
}

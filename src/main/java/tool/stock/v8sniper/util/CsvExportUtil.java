package tool.stock.v8sniper.util;

import tool.stock.v8sniper.dto.StrategyPickView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvExportUtil {
    private CsvExportUtil() {}

    public static Path writeResults(Path target, List<StrategyPickView> rows) throws IOException {
        Files.createDirectories(target.getParent());
        StringBuilder sb = new StringBuilder();
        sb.append("代碼,名稱,產業,總分,股價,漲幅%,成交量,量比,趨勢斜率,建議買入,選股理由,評價\n");
        for (StrategyPickView row : rows) {
            sb.append(csv(row.getStockCode())).append(',')
                    .append(csv(row.getStockName())).append(',')
                    .append(csv(row.getIndustry())).append(',')
                    .append(row.getTotalScore()).append(',')
                    .append(csv(String.valueOf(row.getClosePrice()))).append(',')
                    .append(csv(String.valueOf(row.getPctChange()))).append(',')
                    .append(csv(String.valueOf(row.getVolume()))).append(',')
                    .append(csv(String.valueOf(row.getVolumeRatio()))).append(',')
                    .append(csv(String.valueOf(row.getTrendSlope()))).append(',')
                    .append(csv(String.valueOf(row.getSuggestedBuyPrice()))).append(',')
                    .append(csv(row.getPickReason())).append(',')
                    .append(csv(row.getRating())).append('\n');
        }
        Files.writeString(target, sb.toString(), StandardCharsets.UTF_8);
        return target;
    }

    private static String csv(String value) {
        if (value == null) return "";
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}

package tool.stock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tool.stock.constant.TwseDataset;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TwseSyncService {

    private static final Logger log = LoggerFactory.getLogger(TwseSyncService.class);

    private final TwseDownloadService twseDownloadService;
    private final TwseImportService twseImportService;

    public TwseSyncService(TwseDownloadService twseDownloadService,
                           TwseImportService twseImportService) {
        this.twseDownloadService = twseDownloadService;
        this.twseImportService = twseImportService;
    }

    public Map<String, Object> syncAll() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (TwseDataset dataset : TwseDataset.values()) {
            Map<String, Object> item = new LinkedHashMap<>();

            try {
                log.info("開始同步資料集: {}", dataset.name());

                Path filePath = twseDownloadService.download(dataset);
                int importedRows = twseImportService.importFile(dataset, filePath);

                item.put("success", true);
                item.put("file", filePath.getFileName().toString());
                item.put("rows", importedRows);

                log.info("同步完成: {}, rows={}", dataset.name(), importedRows);

            } catch (Exception e) {
                item.put("success", false);
                item.put("message", e.getMessage());

                log.error("同步失敗: {}", dataset.name(), e);
            }

            result.put(dataset.name(), item);
        }

        return result;
    }
}
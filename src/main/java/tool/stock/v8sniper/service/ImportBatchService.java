package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import tool.stock.v8sniper.entity.ImportBatchEntity;
import tool.stock.v8sniper.repository.ImportBatchRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ImportBatchService {
    private final ImportBatchRepository repository;
    public ImportBatchService(ImportBatchRepository repository) { this.repository = repository; }
    public ImportBatchEntity start(LocalDate date, String sourceType, String apiUrl, String fileName, String filePath) {
        ImportBatchEntity entity = new ImportBatchEntity();
        entity.setBatchDate(date); entity.setSourceType(sourceType); entity.setApiUrl(apiUrl); entity.setSourceFileName(fileName);
        entity.setSourceFilePath(filePath); entity.setImportStatus("STARTED"); entity.setTotalRows(0); entity.setSuccessRows(0); entity.setFailRows(0);
        return repository.save(entity);
    }
    public ImportBatchEntity success(ImportBatchEntity entity, int rows) {
        entity.setImportStatus("SUCCESS"); entity.setTotalRows(rows); entity.setSuccessRows(rows); entity.setFailRows(0); entity.setFinishedAt(LocalDateTime.now());
        return repository.save(entity);
    }
    public ImportBatchEntity fail(ImportBatchEntity entity, int total, int success, String error) {
        entity.setImportStatus("FAILED"); entity.setTotalRows(total); entity.setSuccessRows(success); entity.setFailRows(Math.max(0, total - success)); entity.setErrorMessage(error); entity.setFinishedAt(LocalDateTime.now());
        return repository.save(entity);
    }
}

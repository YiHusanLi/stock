package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.ImportBatch;

/**
 * 匯入批次紀錄存取。
 */
public interface ImportBatchRepository extends JpaRepository<ImportBatch, Long> {
}

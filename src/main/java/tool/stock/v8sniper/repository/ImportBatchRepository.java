package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.ImportBatchEntity;

public interface ImportBatchRepository extends JpaRepository<ImportBatchEntity, Long> {
}

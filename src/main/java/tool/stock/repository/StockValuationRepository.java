package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockValuation;

public interface StockValuationRepository extends JpaRepository<StockValuation, Long> {
}
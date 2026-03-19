package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockDaily;

public interface StockDailyRepository extends JpaRepository<StockDaily, Long> {
}
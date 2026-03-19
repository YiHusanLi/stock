package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockBasic;

public interface StockBasicRepository extends JpaRepository<StockBasic, Long> {
}
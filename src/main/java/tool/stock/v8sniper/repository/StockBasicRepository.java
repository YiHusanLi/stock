package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StockBasicEntity;

import java.util.Optional;

public interface StockBasicRepository extends JpaRepository<StockBasicEntity, Long> {
    Optional<StockBasicEntity> findByStockCode(String stockCode);
}

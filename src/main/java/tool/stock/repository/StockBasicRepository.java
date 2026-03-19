package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockBasic;

import java.util.Optional;

/**
 * 股票基本資料存取。
 */
public interface StockBasicRepository extends JpaRepository<StockBasic, Long> {

    Optional<StockBasic> findByStockCode(String stockCode);
}

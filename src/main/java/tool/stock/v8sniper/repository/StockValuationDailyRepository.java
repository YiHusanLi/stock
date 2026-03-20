package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StockValuationDailyEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface StockValuationDailyRepository extends JpaRepository<StockValuationDailyEntity, Long> {
    Optional<StockValuationDailyEntity> findByTradeDateAndStockCode(LocalDate tradeDate, String stockCode);
}

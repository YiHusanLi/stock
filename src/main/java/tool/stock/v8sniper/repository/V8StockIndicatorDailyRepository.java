package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StockIndicatorDailyEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface V8StockIndicatorDailyRepository extends JpaRepository<StockIndicatorDailyEntity, Long> {
    Optional<StockIndicatorDailyEntity> findByTradeDateAndStockCode(LocalDate tradeDate, String stockCode);
}

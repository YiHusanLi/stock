package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StockChipDailyEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface StockChipDailyRepository extends JpaRepository<StockChipDailyEntity, Long> {
    Optional<StockChipDailyEntity> findByTradeDateAndStockCode(LocalDate tradeDate, String stockCode);
}

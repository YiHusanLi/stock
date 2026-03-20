package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockDailyQuoteRepository extends JpaRepository<StockDailyQuoteEntity, Long> {
    Optional<StockDailyQuoteEntity> findByTradeDateAndStockCode(LocalDate tradeDate, String stockCode);
    List<StockDailyQuoteEntity> findByTradeDateOrderByStockCodeAsc(LocalDate tradeDate);
    List<StockDailyQuoteEntity> findByStockCodeOrderByTradeDateAsc(String stockCode);
}

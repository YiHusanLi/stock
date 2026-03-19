package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockIndicatorDaily;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 指標資料存取。
 */
public interface StockIndicatorDailyRepository extends JpaRepository<StockIndicatorDaily, Long> {

    Optional<StockIndicatorDaily> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);
}

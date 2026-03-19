package tool.stock.v8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8.entity.StockIndicatorDaily;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 指標資料存取。
 */
public interface StockIndicatorDailyRepository extends JpaRepository<StockIndicatorDaily, Long> {

    Optional<StockIndicatorDaily> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);

    List<StockIndicatorDaily> findByTradeDateOrderByStockCodeAsc(LocalDate tradeDate);
}

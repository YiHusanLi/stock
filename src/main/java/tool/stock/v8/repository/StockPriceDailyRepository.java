package tool.stock.v8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8.entity.StockPriceDaily;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 日線資料存取。
 */
public interface StockPriceDailyRepository extends JpaRepository<StockPriceDaily, Long> {

    List<StockPriceDaily> findByTradeDateOrderByStockCodeAsc(LocalDate tradeDate);

    List<StockPriceDaily> findByStockCodeOrderByTradeDateAsc(String stockCode);

    List<StockPriceDaily> findTop60ByStockCodeAndTradeDateLessThanEqualOrderByTradeDateDesc(String stockCode, LocalDate tradeDate);

    Optional<StockPriceDaily> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);

    List<StockPriceDaily> findByTradeDateBetweenOrderByTradeDateAsc(LocalDate startDate, LocalDate endDate);
}

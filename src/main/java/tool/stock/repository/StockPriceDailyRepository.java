package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StockPriceDaily;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 日線資料存取。
 */
public interface StockPriceDailyRepository extends JpaRepository<StockPriceDaily, Long> {

    List<StockPriceDaily> findByTradeDateOrderByStockCodeAsc(LocalDate tradeDate);

    List<StockPriceDaily> findByStockCodeOrderByTradeDateAsc(String stockCode);

    Optional<StockPriceDaily> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);
}

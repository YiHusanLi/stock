package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StrategySignal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 訊號資料存取。
 */
public interface StrategySignalRepository extends JpaRepository<StrategySignal, Long> {

    List<StrategySignal> findByStrategyCodeAndTradeDateOrderByStockCodeAsc(String strategyCode, LocalDate tradeDate);

    List<StrategySignal> findByStrategyCodeAndTradeDateBetweenOrderByTradeDateAscStockCodeAsc(String strategyCode, LocalDate startDate, LocalDate endDate);

    Optional<StrategySignal> findByStrategyCodeAndStockCodeAndTradeDate(String strategyCode, String stockCode, LocalDate tradeDate);

    void deleteByStrategyCodeAndTradeDate(String strategyCode, LocalDate tradeDate);
}

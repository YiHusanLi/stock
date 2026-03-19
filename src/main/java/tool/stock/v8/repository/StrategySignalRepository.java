package tool.stock.v8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8.entity.StrategySignal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 策略訊號存取。
 */
public interface StrategySignalRepository extends JpaRepository<StrategySignal, Long> {

    List<StrategySignal> findByStrategyCodeAndTradeDateOrderByStockCodeAsc(String strategyCode, LocalDate tradeDate);

    List<StrategySignal> findByStrategyCodeAndTradeDateBetweenOrderByTradeDateAscStockCodeAsc(String strategyCode, LocalDate startDate, LocalDate endDate);

    Optional<StrategySignal> findByStrategyCodeAndStockCodeAndTradeDate(String strategyCode, String stockCode, LocalDate tradeDate);

    void deleteByStrategyCodeAndTradeDate(String strategyCode, LocalDate tradeDate);
}

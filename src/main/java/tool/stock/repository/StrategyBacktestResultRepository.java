package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.StrategyBacktestResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 回測資料存取。
 */
public interface StrategyBacktestResultRepository extends JpaRepository<StrategyBacktestResult, Long> {

    List<StrategyBacktestResult> findByStrategyCodeOrderBySignalDateAscStockCodeAsc(String strategyCode);

    List<StrategyBacktestResult> findByStrategyCodeAndSignalDateBetweenOrderBySignalDateAscStockCodeAsc(String strategyCode, LocalDate startDate, LocalDate endDate);

    Optional<StrategyBacktestResult> findByStrategyCodeAndStockCodeAndSignalDate(String strategyCode, String stockCode, LocalDate signalDate);
}

package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StrategyPickResultEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StrategyPickResultRepository extends JpaRepository<StrategyPickResultEntity, Long> {
    List<StrategyPickResultEntity> findByTradeDateOrderByTotalScoreDescStockCodeAsc(LocalDate tradeDate);
    List<StrategyPickResultEntity> findByStockCodeOrderByTradeDateDesc(String stockCode);
    Optional<StrategyPickResultEntity> findByTradeDateAndStockCodeAndStrategyName(LocalDate tradeDate, String stockCode, String strategyName);
    void deleteByTradeDateAndStrategyName(LocalDate tradeDate, String strategyName);
}

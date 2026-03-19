package tool.stock.v8sniper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.v8sniper.entity.StrategyPickDetailEntity;

import java.time.LocalDate;

public interface StrategyPickDetailRepository extends JpaRepository<StrategyPickDetailEntity, Long> {
    void deleteByTradeDateAndStrategyName(LocalDate tradeDate, String strategyName);
}

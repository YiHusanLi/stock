package tool.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tool.stock.entity.RevenueMonthly;

public interface RevenueMonthlyRepository extends JpaRepository<RevenueMonthly, Long> {
}
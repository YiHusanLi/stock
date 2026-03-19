package tool.stock.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 回測結果。
 */
@Entity
@Table(name = "strategy_backtest_result", uniqueConstraints = @UniqueConstraint(name = "uk_strategy_backtest_strategy_stock_date", columnNames = {"strategy_code", "stock_code", "signal_date"}))
public class StrategyBacktestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_code", nullable = false, length = 50)
    private String strategyCode;

    @Column(name = "stock_code", nullable = false, length = 20)
    private String stockCode;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(name = "signal_date", nullable = false)
    private LocalDate signalDate;

    @Column(name = "entry_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal entryPrice;

    @Column(name = "next_day_close", precision = 12, scale = 4)
    private BigDecimal nextDayClose;

    @Column(name = "day3_close", precision = 12, scale = 4)
    private BigDecimal day3Close;

    @Column(name = "day5_close", precision = 12, scale = 4)
    private BigDecimal day5Close;

    @Column(name = "return_1d", precision = 12, scale = 6)
    private BigDecimal return1d;

    @Column(name = "return_3d", precision = 12, scale = 6)
    private BigDecimal return3d;

    @Column(name = "return_5d", precision = 12, scale = 6)
    private BigDecimal return5d;

    @Column(name = "hit_1d", nullable = false)
    private Integer hit1d;

    @Column(name = "hit_3d", nullable = false)
    private Integer hit3d;

    @Column(name = "hit_5d", nullable = false)
    private Integer hit5d;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public String getStrategyCode() { return strategyCode; }
    public void setStrategyCode(String strategyCode) { this.strategyCode = strategyCode; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public LocalDate getSignalDate() { return signalDate; }
    public void setSignalDate(LocalDate signalDate) { this.signalDate = signalDate; }
    public BigDecimal getEntryPrice() { return entryPrice; }
    public void setEntryPrice(BigDecimal entryPrice) { this.entryPrice = entryPrice; }
    public BigDecimal getNextDayClose() { return nextDayClose; }
    public void setNextDayClose(BigDecimal nextDayClose) { this.nextDayClose = nextDayClose; }
    public BigDecimal getDay3Close() { return day3Close; }
    public void setDay3Close(BigDecimal day3Close) { this.day3Close = day3Close; }
    public BigDecimal getDay5Close() { return day5Close; }
    public void setDay5Close(BigDecimal day5Close) { this.day5Close = day5Close; }
    public BigDecimal getReturn1d() { return return1d; }
    public void setReturn1d(BigDecimal return1d) { this.return1d = return1d; }
    public BigDecimal getReturn3d() { return return3d; }
    public void setReturn3d(BigDecimal return3d) { this.return3d = return3d; }
    public BigDecimal getReturn5d() { return return5d; }
    public void setReturn5d(BigDecimal return5d) { this.return5d = return5d; }
    public Integer getHit1d() { return hit1d; }
    public void setHit1d(Integer hit1d) { this.hit1d = hit1d; }
    public Integer getHit3d() { return hit3d; }
    public void setHit3d(Integer hit3d) { this.hit3d = hit3d; }
    public Integer getHit5d() { return hit5d; }
    public void setHit5d(Integer hit5d) { this.hit5d = hit5d; }
}

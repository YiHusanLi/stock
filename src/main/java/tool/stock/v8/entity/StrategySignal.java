package tool.stock.v8.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 策略掃描訊號。
 */
@Entity
@Table(name = "strategy_signal",
        uniqueConstraints = @UniqueConstraint(name = "uk_strategy_signal_code_date_stock", columnNames = {"strategy_code", "stock_code", "trade_date"}))
public class StrategySignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_code", nullable = false, length = 50)
    private String strategyCode;

    @Column(name = "stock_code", nullable = false, length = 20)
    private String stockCode;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(name = "signal_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal signalPrice;

    @Column(name = "pct_change", precision = 10, scale = 4)
    private BigDecimal pctChange;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "ma5", precision = 12, scale = 4)
    private BigDecimal ma5;

    @Column(name = "ma10", precision = 12, scale = 4)
    private BigDecimal ma10;

    @Column(name = "ma20", precision = 12, scale = 4)
    private BigDecimal ma20;

    @Column(name = "squeeze_burst_flag", nullable = false)
    private boolean squeezeBurstFlag;

    @Column(name = "multi_ma_break_flag", nullable = false)
    private boolean multiMaBreakFlag;

    @Column(name = "trend_break_flag", nullable = false)
    private boolean trendBreakFlag;

    @Column(name = "trend_slope", precision = 12, scale = 6)
    private BigDecimal trendSlope;

    @Column(name = "trendline_value", precision = 12, scale = 4)
    private BigDecimal trendlineValue;

    @Column(name = "remark", length = 500)
    private String remark;

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
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public BigDecimal getSignalPrice() { return signalPrice; }
    public void setSignalPrice(BigDecimal signalPrice) { this.signalPrice = signalPrice; }
    public BigDecimal getPctChange() { return pctChange; }
    public void setPctChange(BigDecimal pctChange) { this.pctChange = pctChange; }
    public Long getVolume() { return volume; }
    public void setVolume(Long volume) { this.volume = volume; }
    public BigDecimal getMa5() { return ma5; }
    public void setMa5(BigDecimal ma5) { this.ma5 = ma5; }
    public BigDecimal getMa10() { return ma10; }
    public void setMa10(BigDecimal ma10) { this.ma10 = ma10; }
    public BigDecimal getMa20() { return ma20; }
    public void setMa20(BigDecimal ma20) { this.ma20 = ma20; }
    public boolean isSqueezeBurstFlag() { return squeezeBurstFlag; }
    public void setSqueezeBurstFlag(boolean squeezeBurstFlag) { this.squeezeBurstFlag = squeezeBurstFlag; }
    public boolean isMultiMaBreakFlag() { return multiMaBreakFlag; }
    public void setMultiMaBreakFlag(boolean multiMaBreakFlag) { this.multiMaBreakFlag = multiMaBreakFlag; }
    public boolean isTrendBreakFlag() { return trendBreakFlag; }
    public void setTrendBreakFlag(boolean trendBreakFlag) { this.trendBreakFlag = trendBreakFlag; }
    public BigDecimal getTrendSlope() { return trendSlope; }
    public void setTrendSlope(BigDecimal trendSlope) { this.trendSlope = trendSlope; }
    public BigDecimal getTrendlineValue() { return trendlineValue; }
    public void setTrendlineValue(BigDecimal trendlineValue) { this.trendlineValue = trendlineValue; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

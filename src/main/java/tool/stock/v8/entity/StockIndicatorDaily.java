package tool.stock.v8.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日技術指標資料。
 */
@Entity
@Table(name = "stock_indicator_daily",
        uniqueConstraints = @UniqueConstraint(name = "uk_stock_indicator_daily_code_date", columnNames = {"stock_code", "trade_date"}))
public class StockIndicatorDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code", nullable = false, length = 20)
    private String stockCode;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(name = "ma5", precision = 12, scale = 4)
    private BigDecimal ma5;

    @Column(name = "ma10", precision = 12, scale = 4)
    private BigDecimal ma10;

    @Column(name = "ma20", precision = 12, scale = 4)
    private BigDecimal ma20;

    @Column(name = "ma60", precision = 12, scale = 4)
    private BigDecimal ma60;

    @Column(name = "avg_vol5", precision = 18, scale = 4)
    private BigDecimal avgVol5;

    @Column(name = "avg_vol20", precision = 18, scale = 4)
    private BigDecimal avgVol20;

    @Column(name = "pct_change", precision = 10, scale = 4)
    private BigDecimal pctChange;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public BigDecimal getMa5() { return ma5; }
    public void setMa5(BigDecimal ma5) { this.ma5 = ma5; }
    public BigDecimal getMa10() { return ma10; }
    public void setMa10(BigDecimal ma10) { this.ma10 = ma10; }
    public BigDecimal getMa20() { return ma20; }
    public void setMa20(BigDecimal ma20) { this.ma20 = ma20; }
    public BigDecimal getMa60() { return ma60; }
    public void setMa60(BigDecimal ma60) { this.ma60 = ma60; }
    public BigDecimal getAvgVol5() { return avgVol5; }
    public void setAvgVol5(BigDecimal avgVol5) { this.avgVol5 = avgVol5; }
    public BigDecimal getAvgVol20() { return avgVol20; }
    public void setAvgVol20(BigDecimal avgVol20) { this.avgVol20 = avgVol20; }
    public BigDecimal getPctChange() { return pctChange; }
    public void setPctChange(BigDecimal pctChange) { this.pctChange = pctChange; }
}

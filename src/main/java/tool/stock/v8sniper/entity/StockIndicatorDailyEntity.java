package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_indicator_daily")
public class StockIndicatorDailyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tradeDate;
    private String stockCode;
    private BigDecimal ma5;
    private BigDecimal ma10;
    private BigDecimal ma20;
    private BigDecimal ma60;
    private BigDecimal avgVolume5;
    private BigDecimal avgVolume10;
    private BigDecimal avgVolume20;
    private BigDecimal volumeRatio;
    private BigDecimal trendSlope;
    private BigDecimal trendPressure;
    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public BigDecimal getMa5() { return ma5; }
    public void setMa5(BigDecimal ma5) { this.ma5 = ma5; }
    public BigDecimal getMa10() { return ma10; }
    public void setMa10(BigDecimal ma10) { this.ma10 = ma10; }
    public BigDecimal getMa20() { return ma20; }
    public void setMa20(BigDecimal ma20) { this.ma20 = ma20; }
    public BigDecimal getMa60() { return ma60; }
    public void setMa60(BigDecimal ma60) { this.ma60 = ma60; }
    public BigDecimal getAvgVolume5() { return avgVolume5; }
    public void setAvgVolume5(BigDecimal avgVolume5) { this.avgVolume5 = avgVolume5; }
    public BigDecimal getAvgVolume10() { return avgVolume10; }
    public void setAvgVolume10(BigDecimal avgVolume10) { this.avgVolume10 = avgVolume10; }
    public BigDecimal getAvgVolume20() { return avgVolume20; }
    public void setAvgVolume20(BigDecimal avgVolume20) { this.avgVolume20 = avgVolume20; }
    public BigDecimal getVolumeRatio() { return volumeRatio; }
    public void setVolumeRatio(BigDecimal volumeRatio) { this.volumeRatio = volumeRatio; }
    public BigDecimal getTrendSlope() { return trendSlope; }
    public void setTrendSlope(BigDecimal trendSlope) { this.trendSlope = trendSlope; }
    public BigDecimal getTrendPressure() { return trendPressure; }
    public void setTrendPressure(BigDecimal trendPressure) { this.trendPressure = trendPressure; }
}

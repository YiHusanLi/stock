package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "strategy_pick_result", uniqueConstraints = @UniqueConstraint(name = "uk_trade_date_stock_code_strategy", columnNames = {"trade_date", "stock_code", "strategy_name"}))
public class StrategyPickResultEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tradeDate;
    private String strategyName;
    private String stockCode;
    private String stockName;
    private String industry;
    private Integer totalScore;
    private BigDecimal closePrice;
    private BigDecimal pctChange;
    private Long volume;
    private BigDecimal volumeRatio;
    private BigDecimal trendSlope;
    private BigDecimal suggestedBuyPrice;
    private String pickReason;
    private String rating;
    private boolean isLimitUp;
    private boolean isPreviousDayPicked;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public BigDecimal getClosePrice() { return closePrice; }
    public void setClosePrice(BigDecimal closePrice) { this.closePrice = closePrice; }
    public BigDecimal getPctChange() { return pctChange; }
    public void setPctChange(BigDecimal pctChange) { this.pctChange = pctChange; }
    public Long getVolume() { return volume; }
    public void setVolume(Long volume) { this.volume = volume; }
    public BigDecimal getVolumeRatio() { return volumeRatio; }
    public void setVolumeRatio(BigDecimal volumeRatio) { this.volumeRatio = volumeRatio; }
    public BigDecimal getTrendSlope() { return trendSlope; }
    public void setTrendSlope(BigDecimal trendSlope) { this.trendSlope = trendSlope; }
    public BigDecimal getSuggestedBuyPrice() { return suggestedBuyPrice; }
    public void setSuggestedBuyPrice(BigDecimal suggestedBuyPrice) { this.suggestedBuyPrice = suggestedBuyPrice; }
    public String getPickReason() { return pickReason; }
    public void setPickReason(String pickReason) { this.pickReason = pickReason; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public boolean isLimitUp() { return isLimitUp; }
    public void setLimitUp(boolean limitUp) { isLimitUp = limitUp; }
    public boolean isPreviousDayPicked() { return isPreviousDayPicked; }
    public void setPreviousDayPicked(boolean previousDayPicked) { isPreviousDayPicked = previousDayPicked; }
}

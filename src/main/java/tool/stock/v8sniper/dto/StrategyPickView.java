package tool.stock.v8sniper.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StrategyPickView {
    private LocalDate tradeDate;
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
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
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
}

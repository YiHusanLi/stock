package tool.stock.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 訊號查詢回應。
 */
public class SignalQueryResponse {

    private String stockCode;
    private String stockName;
    private LocalDate tradeDate;
    private BigDecimal closePrice;
    private BigDecimal pctChange;
    private Long volume;
    private BigDecimal ma5;
    private BigDecimal ma10;
    private BigDecimal ma20;
    private boolean squeezeBurst;
    private boolean multiMaBreak;
    private boolean trendBreak;
    private String remark;

    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public BigDecimal getClosePrice() { return closePrice; }
    public void setClosePrice(BigDecimal closePrice) { this.closePrice = closePrice; }
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
    public boolean isSqueezeBurst() { return squeezeBurst; }
    public void setSqueezeBurst(boolean squeezeBurst) { this.squeezeBurst = squeezeBurst; }
    public boolean isMultiMaBreak() { return multiMaBreak; }
    public void setMultiMaBreak(boolean multiMaBreak) { this.multiMaBreak = multiMaBreak; }
    public boolean isTrendBreak() { return trendBreak; }
    public void setTrendBreak(boolean trendBreak) { this.trendBreak = trendBreak; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

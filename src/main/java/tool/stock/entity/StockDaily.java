package tool.stock.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_daily")
public class StockDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate tradeDate;
    private String stockCode;
    private String stockName;
    private Long tradeVolume;
    private Long tradeValue;
    private BigDecimal openingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private BigDecimal closingPrice;
    private BigDecimal priceChange;
    private Long transactionCount;

    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public Long getTradeVolume() { return tradeVolume; }
    public void setTradeVolume(Long tradeVolume) { this.tradeVolume = tradeVolume; }
    public Long getTradeValue() { return tradeValue; }
    public void setTradeValue(Long tradeValue) { this.tradeValue = tradeValue; }
    public BigDecimal getOpeningPrice() { return openingPrice; }
    public void setOpeningPrice(BigDecimal openingPrice) { this.openingPrice = openingPrice; }
    public BigDecimal getHighestPrice() { return highestPrice; }
    public void setHighestPrice(BigDecimal highestPrice) { this.highestPrice = highestPrice; }
    public BigDecimal getLowestPrice() { return lowestPrice; }
    public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
    public BigDecimal getClosingPrice() { return closingPrice; }
    public void setClosingPrice(BigDecimal closingPrice) { this.closingPrice = closingPrice; }
    public BigDecimal getPriceChange() { return priceChange; }
    public void setPriceChange(BigDecimal priceChange) { this.priceChange = priceChange; }
    public Long getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Long transactionCount) { this.transactionCount = transactionCount; }
}
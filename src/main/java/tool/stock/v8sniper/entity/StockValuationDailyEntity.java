package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_valuation_daily")
public class StockValuationDailyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tradeDate;
    private String stockCode;
    private BigDecimal peRatio;
    private BigDecimal dividendYield;
    private BigDecimal pbRatio;
    private BigDecimal eps;
    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public BigDecimal getPeRatio() { return peRatio; }
    public void setPeRatio(BigDecimal peRatio) { this.peRatio = peRatio; }
    public BigDecimal getDividendYield() { return dividendYield; }
    public void setDividendYield(BigDecimal dividendYield) { this.dividendYield = dividendYield; }
    public BigDecimal getPbRatio() { return pbRatio; }
    public void setPbRatio(BigDecimal pbRatio) { this.pbRatio = pbRatio; }
    public BigDecimal getEps() { return eps; }
    public void setEps(BigDecimal eps) { this.eps = eps; }
}

package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_chip_daily")
public class StockChipDailyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tradeDate;
    private String stockCode;
    private Long foreignNetBuy;
    private Long investmentTrustNetBuy;
    private Long dealerNetBuy;
    private Long marginBalance;
    private Long shortBalance;
    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public Long getForeignNetBuy() { return foreignNetBuy; }
    public void setForeignNetBuy(Long foreignNetBuy) { this.foreignNetBuy = foreignNetBuy; }
    public Long getInvestmentTrustNetBuy() { return investmentTrustNetBuy; }
    public void setInvestmentTrustNetBuy(Long investmentTrustNetBuy) { this.investmentTrustNetBuy = investmentTrustNetBuy; }
    public Long getDealerNetBuy() { return dealerNetBuy; }
    public void setDealerNetBuy(Long dealerNetBuy) { this.dealerNetBuy = dealerNetBuy; }
    public Long getMarginBalance() { return marginBalance; }
    public void setMarginBalance(Long marginBalance) { this.marginBalance = marginBalance; }
    public Long getShortBalance() { return shortBalance; }
    public void setShortBalance(Long shortBalance) { this.shortBalance = shortBalance; }
}

package tool.stock.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 股票基本資料主檔。
 */
@Entity
@Table(name = "stock_basic", uniqueConstraints = @UniqueConstraint(name = "uk_stock_basic_code", columnNames = {"stock_code"}))
public class StockBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code", nullable = false, length = 20)
    private String stockCode;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(name = "market_type", length = 20)
    private String marketType;

    @Column(name = "industry_type", length = 100)
    private String industryType;

    @Column(name = "isin_code", length = 20)
    private String isinCode;

    @Column(name = "listing_date")
    private LocalDate listingDate;

    @Column(name = "update_time", insertable = false, updatable = false)
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getMarketType() { return marketType; }
    public void setMarketType(String marketType) { this.marketType = marketType; }
    public String getIndustryType() { return industryType; }
    public void setIndustryType(String industryType) { this.industryType = industryType; }
    public String getIsinCode() { return isinCode; }
    public void setIsinCode(String isinCode) { this.isinCode = isinCode; }
    public LocalDate getListingDate() { return listingDate; }
    public void setListingDate(LocalDate listingDate) { this.listingDate = listingDate; }
}

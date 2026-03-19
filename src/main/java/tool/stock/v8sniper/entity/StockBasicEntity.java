package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_basic", uniqueConstraints = @UniqueConstraint(name = "uk_stock_basic_code", columnNames = "stock_code"))
public class StockBasicEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "stock_code", nullable = false, length = 20)
    private String stockCode;
    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;
    @Column(name = "market_type", length = 30)
    private String marketType;
    @Column(name = "industry", length = 100)
    private String industry;
    @Column(name = "is_etf", nullable = false)
    private boolean etf;
    @Column(name = "listing_type", length = 30)
    private String listingType;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
    public Long getId() { return id; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getMarketType() { return marketType; }
    public void setMarketType(String marketType) { this.marketType = marketType; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public boolean isEtf() { return etf; }
    public void setEtf(boolean etf) { this.etf = etf; }
    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }
}

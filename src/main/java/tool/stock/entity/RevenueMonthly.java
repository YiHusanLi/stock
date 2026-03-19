package tool.stock.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "revenue_monthly")
public class RevenueMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dataYear;
    private Integer dataMonth;
    private String stockCode;
    private String stockName;
    private String industryType;
    private Long revenueCurrent;
    private Long revenueLastMonth;
    private Long revenueLastYear;
    private BigDecimal revenueMom;
    private BigDecimal revenueYoy;
    private Long cumulativeRevenueCurrent;
    private Long cumulativeRevenueLastYear;
    private BigDecimal cumulativeGrowth;
    private String note;

    public Long getId() { return id; }
    public Integer getDataYear() { return dataYear; }
    public void setDataYear(Integer dataYear) { this.dataYear = dataYear; }
    public Integer getDataMonth() { return dataMonth; }
    public void setDataMonth(Integer dataMonth) { this.dataMonth = dataMonth; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getIndustryType() { return industryType; }
    public void setIndustryType(String industryType) { this.industryType = industryType; }
    public Long getRevenueCurrent() { return revenueCurrent; }
    public void setRevenueCurrent(Long revenueCurrent) { this.revenueCurrent = revenueCurrent; }
    public Long getRevenueLastMonth() { return revenueLastMonth; }
    public void setRevenueLastMonth(Long revenueLastMonth) { this.revenueLastMonth = revenueLastMonth; }
    public Long getRevenueLastYear() { return revenueLastYear; }
    public void setRevenueLastYear(Long revenueLastYear) { this.revenueLastYear = revenueLastYear; }
    public BigDecimal getRevenueMom() { return revenueMom; }
    public void setRevenueMom(BigDecimal revenueMom) { this.revenueMom = revenueMom; }
    public BigDecimal getRevenueYoy() { return revenueYoy; }
    public void setRevenueYoy(BigDecimal revenueYoy) { this.revenueYoy = revenueYoy; }
    public Long getCumulativeRevenueCurrent() { return cumulativeRevenueCurrent; }
    public void setCumulativeRevenueCurrent(Long cumulativeRevenueCurrent) { this.cumulativeRevenueCurrent = cumulativeRevenueCurrent; }
    public Long getCumulativeRevenueLastYear() { return cumulativeRevenueLastYear; }
    public void setCumulativeRevenueLastYear(Long cumulativeRevenueLastYear) { this.cumulativeRevenueLastYear = cumulativeRevenueLastYear; }
    public BigDecimal getCumulativeGrowth() { return cumulativeGrowth; }
    public void setCumulativeGrowth(BigDecimal cumulativeGrowth) { this.cumulativeGrowth = cumulativeGrowth; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
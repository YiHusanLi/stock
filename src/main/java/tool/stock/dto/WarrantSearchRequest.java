package tool.stock.dto;

import java.math.BigDecimal;

public class WarrantSearchRequest {

    private String underlyingCode;
    private String warrantType; // CALL / PUT
    private Integer minDaysToMaturity;
    private Integer maxDaysToMaturity;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Long minTradeVolume;

    private BigDecimal minMoneynessPercent;
    private BigDecimal maxMoneynessPercent;

    private String issuerName;

    private String sortBy;     // volume / spread / days / score
    private String sortOrder;  // asc / desc

    public String getUnderlyingCode() {
        return underlyingCode;
    }

    public void setUnderlyingCode(String underlyingCode) {
        this.underlyingCode = underlyingCode;
    }

    public String getWarrantType() {
        return warrantType;
    }

    public void setWarrantType(String warrantType) {
        this.warrantType = warrantType;
    }

    public Integer getMinDaysToMaturity() {
        return minDaysToMaturity;
    }

    public void setMinDaysToMaturity(Integer minDaysToMaturity) {
        this.minDaysToMaturity = minDaysToMaturity;
    }

    public Integer getMaxDaysToMaturity() {
        return maxDaysToMaturity;
    }

    public void setMaxDaysToMaturity(Integer maxDaysToMaturity) {
        this.maxDaysToMaturity = maxDaysToMaturity;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getMinTradeVolume() {
        return minTradeVolume;
    }

    public void setMinTradeVolume(Long minTradeVolume) {
        this.minTradeVolume = minTradeVolume;
    }

    public BigDecimal getMinMoneynessPercent() {
        return minMoneynessPercent;
    }

    public void setMinMoneynessPercent(BigDecimal minMoneynessPercent) {
        this.minMoneynessPercent = minMoneynessPercent;
    }

    public BigDecimal getMaxMoneynessPercent() {
        return maxMoneynessPercent;
    }

    public void setMaxMoneynessPercent(BigDecimal maxMoneynessPercent) {
        this.maxMoneynessPercent = maxMoneynessPercent;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
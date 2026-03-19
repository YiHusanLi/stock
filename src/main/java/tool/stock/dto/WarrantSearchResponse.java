package tool.stock.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WarrantSearchResponse {

    private String warrantCode;
    private String warrantName;
    private String underlyingCode;
    private String underlyingName;
    private String warrantType;
    private String issuerName;

    private BigDecimal underlyingClosePrice;
    private BigDecimal warrantClosePrice;
    private BigDecimal strikePrice;

    private Integer daysToMaturity;
    private BigDecimal moneynessPercent;
    private Long tradeVolume;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private BigDecimal spreadPercent;

    private BigDecimal score;
    private LocalDate tradeDate;
    private LocalDate maturityDate;

    public String getWarrantCode() {
        return warrantCode;
    }

    public void setWarrantCode(String warrantCode) {
        this.warrantCode = warrantCode;
    }

    public String getWarrantName() {
        return warrantName;
    }

    public void setWarrantName(String warrantName) {
        this.warrantName = warrantName;
    }

    public String getUnderlyingCode() {
        return underlyingCode;
    }

    public void setUnderlyingCode(String underlyingCode) {
        this.underlyingCode = underlyingCode;
    }

    public String getUnderlyingName() {
        return underlyingName;
    }

    public void setUnderlyingName(String underlyingName) {
        this.underlyingName = underlyingName;
    }

    public String getWarrantType() {
        return warrantType;
    }

    public void setWarrantType(String warrantType) {
        this.warrantType = warrantType;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public BigDecimal getUnderlyingClosePrice() {
        return underlyingClosePrice;
    }

    public void setUnderlyingClosePrice(BigDecimal underlyingClosePrice) {
        this.underlyingClosePrice = underlyingClosePrice;
    }

    public BigDecimal getWarrantClosePrice() {
        return warrantClosePrice;
    }

    public void setWarrantClosePrice(BigDecimal warrantClosePrice) {
        this.warrantClosePrice = warrantClosePrice;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }

    public Integer getDaysToMaturity() {
        return daysToMaturity;
    }

    public void setDaysToMaturity(Integer daysToMaturity) {
        this.daysToMaturity = daysToMaturity;
    }

    public BigDecimal getMoneynessPercent() {
        return moneynessPercent;
    }

    public void setMoneynessPercent(BigDecimal moneynessPercent) {
        this.moneynessPercent = moneynessPercent;
    }

    public Long getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getSpreadPercent() {
        return spreadPercent;
    }

    public void setSpreadPercent(BigDecimal spreadPercent) {
        this.spreadPercent = spreadPercent;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }
}
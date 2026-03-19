package tool.stock.dto.twse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevenueMonthlyDto {

    @JsonProperty("出表日期")
    private String reportDate;

    @JsonProperty("資料年月")
    private String dataYearMonth;

    @JsonProperty("公司代號")
    private String stockCode;

    @JsonProperty("公司名稱")
    private String stockName;

    @JsonProperty("產業別")
    private String industryType;

    @JsonProperty("營業收入-當月營收")
    private String revenueCurrent;

    @JsonProperty("營業收入-上月營收")
    private String revenueLastMonth;

    @JsonProperty("營業收入-去年當月營收")
    private String revenueLastYear;

    @JsonProperty("營業收入-上月比較增減(%)")
    private String revenueMom;

    @JsonProperty("營業收入-去年同月增減(%)")
    private String revenueYoy;

    @JsonProperty("累計營業收入-當月累計營收")
    private String cumulativeRevenueCurrent;

    @JsonProperty("累計營業收入-去年累計營收")
    private String cumulativeRevenueLastYear;

    @JsonProperty("累計營業收入-前期比較增減(%)")
    private String cumulativeGrowth;

    @JsonProperty("備註")
    private String note;

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getDataYearMonth() {
        return dataYearMonth;
    }

    public void setDataYearMonth(String dataYearMonth) {
        this.dataYearMonth = dataYearMonth;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getRevenueCurrent() {
        return revenueCurrent;
    }

    public void setRevenueCurrent(String revenueCurrent) {
        this.revenueCurrent = revenueCurrent;
    }

    public String getRevenueLastMonth() {
        return revenueLastMonth;
    }

    public void setRevenueLastMonth(String revenueLastMonth) {
        this.revenueLastMonth = revenueLastMonth;
    }

    public String getRevenueLastYear() {
        return revenueLastYear;
    }

    public void setRevenueLastYear(String revenueLastYear) {
        this.revenueLastYear = revenueLastYear;
    }

    public String getRevenueMom() {
        return revenueMom;
    }

    public void setRevenueMom(String revenueMom) {
        this.revenueMom = revenueMom;
    }

    public String getRevenueYoy() {
        return revenueYoy;
    }

    public void setRevenueYoy(String revenueYoy) {
        this.revenueYoy = revenueYoy;
    }

    public String getCumulativeRevenueCurrent() {
        return cumulativeRevenueCurrent;
    }

    public void setCumulativeRevenueCurrent(String cumulativeRevenueCurrent) {
        this.cumulativeRevenueCurrent = cumulativeRevenueCurrent;
    }

    public String getCumulativeRevenueLastYear() {
        return cumulativeRevenueLastYear;
    }

    public void setCumulativeRevenueLastYear(String cumulativeRevenueLastYear) {
        this.cumulativeRevenueLastYear = cumulativeRevenueLastYear;
    }

    public String getCumulativeGrowth() {
        return cumulativeGrowth;
    }

    public void setCumulativeGrowth(String cumulativeGrowth) {
        this.cumulativeGrowth = cumulativeGrowth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
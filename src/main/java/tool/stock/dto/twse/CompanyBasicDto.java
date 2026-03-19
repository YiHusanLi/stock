package tool.stock.dto.twse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyBasicDto {

    @JsonProperty("出表日期")
    private String reportDate;

    @JsonProperty("公司代號")
    private String stockCode;

    @JsonProperty("公司名稱")
    private String companyName;

    @JsonProperty("公司簡稱")
    private String companyShortName;

    @JsonProperty("英文簡稱")
    private String englishShortName;

    @JsonProperty("產業別")
    private String industryType;

    @JsonProperty("董事長")
    private String chairman;

    @JsonProperty("總經理")
    private String generalManager;

    @JsonProperty("發言人")
    private String spokesperson;

    @JsonProperty("住址")
    private String address;

    @JsonProperty("總機電話")
    private String phone;

    @JsonProperty("成立日期")
    private String establishmentDate;

    @JsonProperty("上市日期")
    private String listingDate;

    @JsonProperty("實收資本額")
    private String capitalAmount;

    @JsonProperty("網址")
    private String website;

    @JsonProperty("已發行普通股數或TDR原股發行股數")
    private String issuedCommonShares;

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getEnglishShortName() {
        return englishShortName;
    }

    public void setEnglishShortName(String englishShortName) {
        this.englishShortName = englishShortName;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getChairman() {
        return chairman;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    public String getGeneralManager() {
        return generalManager;
    }

    public void setGeneralManager(String generalManager) {
        this.generalManager = generalManager;
    }

    public String getSpokesperson() {
        return spokesperson;
    }

    public void setSpokesperson(String spokesperson) {
        this.spokesperson = spokesperson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public String getListingDate() {
        return listingDate;
    }

    public void setListingDate(String listingDate) {
        this.listingDate = listingDate;
    }

    public String getCapitalAmount() {
        return capitalAmount;
    }

    public void setCapitalAmount(String capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIssuedCommonShares() {
        return issuedCommonShares;
    }

    public void setIssuedCommonShares(String issuedCommonShares) {
        this.issuedCommonShares = issuedCommonShares;
    }
}
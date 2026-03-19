package tool.stock.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_basic")
public class StockBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_short_name")
    private String companyShortName;

    @Column(name = "english_short_name")
    private String englishShortName;

    @Column(name = "industry_type")
    private String industryType;

    @Column(name = "chairman")
    private String chairman;

    @Column(name = "general_manager")
    private String generalManager;

    @Column(name = "spokesperson")
    private String spokesperson;

    @Column(name = "capital_amount")
    private Long capitalAmount;

    @Column(name = "issued_common_shares")
    private Long issuedCommonShares;

    @Column(name = "listing_date")
    private LocalDate listingDate;

    @Column(name = "establishment_date")
    private LocalDate establishmentDate;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "website")
    private String website;

    public Long getId() {
        return id;
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

    public Long getCapitalAmount() {
        return capitalAmount;
    }

    public void setCapitalAmount(Long capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    public Long getIssuedCommonShares() {
        return issuedCommonShares;
    }

    public void setIssuedCommonShares(Long issuedCommonShares) {
        this.issuedCommonShares = issuedCommonShares;
    }

    public LocalDate getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDate listingDate) {
        this.listingDate = listingDate;
    }

    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(LocalDate establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
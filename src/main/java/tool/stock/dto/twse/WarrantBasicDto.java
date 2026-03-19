package tool.stock.dto.twse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WarrantBasicDto {

    @JsonProperty("出表日期")
    private String reportDate;

    @JsonProperty("權證代號")
    private String warrantCode;

    @JsonProperty("權證簡稱")
    private String warrantName;

    @JsonProperty("權證類型")
    private String warrantType;

    @JsonProperty("類別")
    private String warrantCategory;

    @JsonProperty("流動量提供者報價方式")
    private String quoteMethod;

    @JsonProperty("履約開始日")
    private String exerciseStartDate;

    @JsonProperty("最後交易日")
    private String lastTradingDate;

    @JsonProperty("履約截止日")
    private String exerciseEndDate;

    @JsonProperty("發行單位數量(仟單位)")
    private String issueUnitThousand;

    @JsonProperty("結算方式(詳附註編號說明)")
    private String settlementMethod;

    @JsonProperty("標的證券/指數")
    private String underlyingSymbol;

    @JsonProperty("最新標的履約配發數量(每仟單位權證)")
    private String latestEntitlementRatio;

    @JsonProperty("原始履約價格(元)/履約指數")
    private String originalStrikePrice;

    @JsonProperty("原始上限價格(元)/上限指數")
    private String originalUpperPrice;

    @JsonProperty("原始下限價格(元)/下限指數")
    private String originalLowerPrice;

    @JsonProperty("最新履約價格(元)/履約指數")
    private String latestStrikePrice;

    @JsonProperty("最新上限價格(元)/上限指數")
    private String latestUpperPrice;

    @JsonProperty("最新下限價格(元)/下限指數")
    private String latestLowerPrice;

    @JsonProperty("備註")
    private String note;
}
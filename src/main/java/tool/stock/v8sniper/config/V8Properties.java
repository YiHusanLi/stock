package tool.stock.v8sniper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * V8.0 狙擊主力籌碼突破版設定。
 */
@Component
@ConfigurationProperties(prefix = "app")
public class V8Properties {

    private String importMode = "API_AND_FILE";
    private String dataRoot = "./data";
    private final Twse twse = new Twse();
    private final Strategy strategy = new Strategy();

    public String getImportMode() { return importMode; }
    public void setImportMode(String importMode) { this.importMode = importMode; }
    public String getDataRoot() { return dataRoot; }
    public void setDataRoot(String dataRoot) { this.dataRoot = dataRoot; }
    public Twse getTwse() { return twse; }
    public Strategy getStrategy() { return strategy; }

    public static class Twse {
        private String baseUrl;
        private String dailyQuoteEndpoint;
        private String valuationEndpoint;
        private String stockBasicEndpoint;
        private String chipEndpoint;
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getDailyQuoteEndpoint() { return dailyQuoteEndpoint; }
        public void setDailyQuoteEndpoint(String dailyQuoteEndpoint) { this.dailyQuoteEndpoint = dailyQuoteEndpoint; }
        public String getValuationEndpoint() { return valuationEndpoint; }
        public void setValuationEndpoint(String valuationEndpoint) { this.valuationEndpoint = valuationEndpoint; }
        public String getStockBasicEndpoint() { return stockBasicEndpoint; }
        public void setStockBasicEndpoint(String stockBasicEndpoint) { this.stockBasicEndpoint = stockBasicEndpoint; }
        public String getChipEndpoint() { return chipEndpoint; }
        public void setChipEndpoint(String chipEndpoint) { this.chipEndpoint = chipEndpoint; }
    }

    public static class Strategy {
        private final V8 v8 = new V8();
        public V8 getV8() { return v8; }
    }

    public static class V8 {
        private String strategyName = "V8.0 狙擊主力籌碼突破版";
        private BigDecimal minPrice = BigDecimal.TEN;
        private long minVolume = 500000L;
        private BigDecimal minPctChange = BigDecimal.valueOf(4.0);
        private final Volume volume = new Volume();
        private final Ma ma = new Ma();
        private final Trend trend = new Trend();
        private final BuyPrice buyPrice = new BuyPrice();
        private final Score score = new Score();
        public String getStrategyName() { return strategyName; }
        public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
        public BigDecimal getMinPrice() { return minPrice; }
        public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
        public long getMinVolume() { return minVolume; }
        public void setMinVolume(long minVolume) { this.minVolume = minVolume; }
        public BigDecimal getMinPctChange() { return minPctChange; }
        public void setMinPctChange(BigDecimal minPctChange) { this.minPctChange = minPctChange; }
        public Volume getVolume() { return volume; }
        public Ma getMa() { return ma; }
        public Trend getTrend() { return trend; }
        public BuyPrice getBuyPrice() { return buyPrice; }
        public Score getScore() { return score; }
    }

    public static class Volume {
        private int shrinkDays = 5;
        private int breakoutDays5 = 5;
        private int breakoutDays10 = 10;
        private BigDecimal breakoutMultiplier5 = BigDecimal.valueOf(2.0);
        private BigDecimal breakoutMultiplier10 = BigDecimal.valueOf(1.5);
        public int getShrinkDays() { return shrinkDays; }
        public void setShrinkDays(int shrinkDays) { this.shrinkDays = shrinkDays; }
        public int getBreakoutDays5() { return breakoutDays5; }
        public void setBreakoutDays5(int breakoutDays5) { this.breakoutDays5 = breakoutDays5; }
        public int getBreakoutDays10() { return breakoutDays10; }
        public void setBreakoutDays10(int breakoutDays10) { this.breakoutDays10 = breakoutDays10; }
        public BigDecimal getBreakoutMultiplier5() { return breakoutMultiplier5; }
        public void setBreakoutMultiplier5(BigDecimal breakoutMultiplier5) { this.breakoutMultiplier5 = breakoutMultiplier5; }
        public BigDecimal getBreakoutMultiplier10() { return breakoutMultiplier10; }
        public void setBreakoutMultiplier10(BigDecimal breakoutMultiplier10) { this.breakoutMultiplier10 = breakoutMultiplier10; }
    }

    public static class Ma {
        private String mode = "PRICE_OVER_MULTI_MA";
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
    }

    public static class Trend {
        private String mode = "REGRESSION";
        private int lookbackDays = 20;
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public int getLookbackDays() { return lookbackDays; }
        public void setLookbackDays(int lookbackDays) { this.lookbackDays = lookbackDays; }
    }

    public static class BuyPrice {
        private String mode = "CLOSE_DISCOUNT";
        private BigDecimal discountRate = BigDecimal.valueOf(0.97);
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public BigDecimal getDiscountRate() { return discountRate; }
        public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    }

    public static class Score {
        private int weightVolumeBreakout = 30;
        private int weightMaBreakout = 25;
        private int weightTrendBreakout = 25;
        private int weightPctStrength = 10;
        private int weightVolumeThreshold = 10;
        public int getWeightVolumeBreakout() { return weightVolumeBreakout; }
        public void setWeightVolumeBreakout(int weightVolumeBreakout) { this.weightVolumeBreakout = weightVolumeBreakout; }
        public int getWeightMaBreakout() { return weightMaBreakout; }
        public void setWeightMaBreakout(int weightMaBreakout) { this.weightMaBreakout = weightMaBreakout; }
        public int getWeightTrendBreakout() { return weightTrendBreakout; }
        public void setWeightTrendBreakout(int weightTrendBreakout) { this.weightTrendBreakout = weightTrendBreakout; }
        public int getWeightPctStrength() { return weightPctStrength; }
        public void setWeightPctStrength(int weightPctStrength) { this.weightPctStrength = weightPctStrength; }
        public int getWeightVolumeThreshold() { return weightVolumeThreshold; }
        public void setWeightVolumeThreshold(int weightVolumeThreshold) { this.weightVolumeThreshold = weightVolumeThreshold; }
    }
}

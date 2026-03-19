package tool.stock.v8sniper.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "strategy_pick_detail")
public class StrategyPickDetailEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tradeDate;
    private String stockCode;
    private String strategyName;
    private String ruleCode;
    private String ruleName;
    private Integer score;
    private boolean passed;
    private String ruleValue;
    private String ruleThreshold;
    public Long getId() { return id; }
    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    public String getRuleValue() { return ruleValue; }
    public void setRuleValue(String ruleValue) { this.ruleValue = ruleValue; }
    public String getRuleThreshold() { return ruleThreshold; }
    public void setRuleThreshold(String ruleThreshold) { this.ruleThreshold = ruleThreshold; }
}

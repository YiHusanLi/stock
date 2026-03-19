package tool.stock.v8sniper.dto;

public class RuleEvaluation {
    private final String ruleCode;
    private final String ruleName;
    private final boolean passed;
    private final int score;
    private final String ruleValue;
    private final String threshold;
    private final String reason;
    public RuleEvaluation(String ruleCode, String ruleName, boolean passed, int score, String ruleValue, String threshold, String reason) {
        this.ruleCode = ruleCode; this.ruleName = ruleName; this.passed = passed; this.score = score; this.ruleValue = ruleValue; this.threshold = threshold; this.reason = reason;
    }
    public String getRuleCode() { return ruleCode; }
    public String getRuleName() { return ruleName; }
    public boolean isPassed() { return passed; }
    public int getScore() { return score; }
    public String getRuleValue() { return ruleValue; }
    public String getThreshold() { return threshold; }
    public String getReason() { return reason; }
}

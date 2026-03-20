package tool.stock.v8sniper.dto;

public class ImportSummary {
    private String sourceType;
    private int totalRows;
    private long batchId;
    private String message;
    public ImportSummary(String sourceType, int totalRows, long batchId, String message) {
        this.sourceType = sourceType; this.totalRows = totalRows; this.batchId = batchId; this.message = message;
    }
    public String getSourceType() { return sourceType; }
    public int getTotalRows() { return totalRows; }
    public long getBatchId() { return batchId; }
    public String getMessage() { return message; }
}

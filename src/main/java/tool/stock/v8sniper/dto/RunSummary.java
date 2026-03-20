package tool.stock.v8sniper.dto;

import java.util.List;

public class RunSummary {
    private final int pickedCount;
    private final String exportPath;
    private final List<String> messages;
    public RunSummary(int pickedCount, String exportPath, List<String> messages) {
        this.pickedCount = pickedCount; this.exportPath = exportPath; this.messages = messages;
    }
    public int getPickedCount() { return pickedCount; }
    public String getExportPath() { return exportPath; }
    public List<String> getMessages() { return messages; }
}

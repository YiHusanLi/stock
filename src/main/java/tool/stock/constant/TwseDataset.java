package tool.stock.constant;

public enum TwseDataset {

    STOCK_DAY_ALL("/exchangeReport/STOCK_DAY_ALL", "stock_day_all"),
    BWIBBU_ALL("/exchangeReport/BWIBBU_ALL", "bwibbu_all"),
    T86("/fund/T86", "t86");

    private final String path;
    private final String filePrefix;

    TwseDataset(String path, String filePrefix) {
        this.path = path;
        this.filePrefix = filePrefix;
    }

    public String getPath() {
        return path;
    }

    public String getFilePrefix() {
        return filePrefix;
    }
}
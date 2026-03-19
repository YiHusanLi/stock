package tool.stock.dto.twse;

public class StockDayAllDto {

    private String Date;
    private String Code;
    private String Name;
    private String TradeVolume;
    private String TradeValue;
    private String OpeningPrice;
    private String HighestPrice;
    private String LowestPrice;
    private String ClosingPrice;
    private String Change;
    private String Transaction;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTradeVolume() {
        return TradeVolume;
    }

    public void setTradeVolume(String tradeVolume) {
        TradeVolume = tradeVolume;
    }

    public String getTradeValue() {
        return TradeValue;
    }

    public void setTradeValue(String tradeValue) {
        TradeValue = tradeValue;
    }

    public String getOpeningPrice() {
        return OpeningPrice;
    }

    public void setOpeningPrice(String openingPrice) {
        OpeningPrice = openingPrice;
    }

    public String getHighestPrice() {
        return HighestPrice;
    }

    public void setHighestPrice(String highestPrice) {
        HighestPrice = highestPrice;
    }

    public String getLowestPrice() {
        return LowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        LowestPrice = lowestPrice;
    }

    public String getClosingPrice() {
        return ClosingPrice;
    }

    public void setClosingPrice(String closingPrice) {
        ClosingPrice = closingPrice;
    }

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }
}
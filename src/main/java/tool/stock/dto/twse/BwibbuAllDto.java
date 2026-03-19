package tool.stock.dto.twse;

public class BwibbuAllDto {

    private String Date;
    private String Code;
    private String Name;
    private String PEratio;
    private String DividendYield;
    private String PBratio;

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

    public String getPEratio() {
        return PEratio;
    }

    public void setPEratio(String pEratio) {
        PEratio = pEratio;
    }

    public String getDividendYield() {
        return DividendYield;
    }

    public void setDividendYield(String dividendYield) {
        DividendYield = dividendYield;
    }

    public String getPBratio() {
        return PBratio;
    }

    public void setPBratio(String pBratio) {
        PBratio = pBratio;
    }
}
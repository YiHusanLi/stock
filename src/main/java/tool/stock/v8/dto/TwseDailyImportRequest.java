package tool.stock.v8.dto;

import java.time.LocalDate;

/**
 * 匯入 TWSE 日線資料的請求。
 */
public class TwseDailyImportRequest {

    private LocalDate tradeDate;

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }
}

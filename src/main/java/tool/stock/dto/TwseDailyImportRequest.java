package tool.stock.dto;

import java.time.LocalDate;

/**
 * TWSE 日線匯入請求。
 */
public class TwseDailyImportRequest {

    private LocalDate tradeDate;

    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
}

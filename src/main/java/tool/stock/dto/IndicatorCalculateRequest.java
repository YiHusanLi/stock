package tool.stock.dto;

import java.time.LocalDate;

/**
 * 指標計算請求。
 */
public class IndicatorCalculateRequest {

    private LocalDate tradeDate;

    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }
}

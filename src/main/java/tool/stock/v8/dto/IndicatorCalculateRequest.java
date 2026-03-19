package tool.stock.v8.dto;

import java.time.LocalDate;

/**
 * 計算技術指標的請求。
 */
public class IndicatorCalculateRequest {

    private LocalDate tradeDate;

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }
}

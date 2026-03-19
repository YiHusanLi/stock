package tool.stock.dto;

import java.time.LocalDate;

/**
 * 回測請求。
 */
public class BacktestRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}

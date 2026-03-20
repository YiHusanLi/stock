package tool.stock.v8sniper.dto;

import tool.stock.v8sniper.entity.StockBasicEntity;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;
import tool.stock.v8sniper.entity.StockIndicatorDailyEntity;

import java.util.List;

public class StockSnapshot {
    private final StockBasicEntity basic;
    private final StockDailyQuoteEntity today;
    private final StockIndicatorDailyEntity indicator;
    private final List<StockDailyQuoteEntity> history;
    private final boolean previousDayPicked;

    public StockSnapshot(StockBasicEntity basic, StockDailyQuoteEntity today, StockIndicatorDailyEntity indicator,
                         List<StockDailyQuoteEntity> history, boolean previousDayPicked) {
        this.basic = basic; this.today = today; this.indicator = indicator; this.history = history; this.previousDayPicked = previousDayPicked;
    }
    public StockBasicEntity getBasic() { return basic; }
    public StockDailyQuoteEntity getToday() { return today; }
    public StockIndicatorDailyEntity getIndicator() { return indicator; }
    public List<StockDailyQuoteEntity> getHistory() { return history; }
    public boolean isPreviousDayPicked() { return previousDayPicked; }
}

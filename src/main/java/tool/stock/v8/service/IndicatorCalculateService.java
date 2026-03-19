package tool.stock.v8.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8.entity.StockIndicatorDaily;
import tool.stock.v8.entity.StockPriceDaily;
import tool.stock.v8.repository.StockIndicatorDailyRepository;
import tool.stock.v8.repository.StockPriceDailyRepository;
import tool.stock.v8.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 計算均線、均量與漲跌幅。
 */
@Service
public class IndicatorCalculateService {

    private final StockPriceDailyRepository stockPriceDailyRepository;
    private final StockIndicatorDailyRepository stockIndicatorDailyRepository;

    public IndicatorCalculateService(StockPriceDailyRepository stockPriceDailyRepository,
                                     StockIndicatorDailyRepository stockIndicatorDailyRepository) {
        this.stockPriceDailyRepository = stockPriceDailyRepository;
        this.stockIndicatorDailyRepository = stockIndicatorDailyRepository;
    }

    @Transactional
    public int calculate(LocalDate tradeDate) {
        List<StockPriceDaily> allPrices = stockPriceDailyRepository.findAll();
        Map<String, List<StockPriceDaily>> grouped = allPrices.stream()
                .sorted(Comparator.comparing(StockPriceDaily::getTradeDate))
                .collect(Collectors.groupingBy(StockPriceDaily::getStockCode, Collectors.toList()));

        int count = 0;
        for (Map.Entry<String, List<StockPriceDaily>> entry : grouped.entrySet()) {
            List<StockPriceDaily> series = entry.getValue();
            for (int i = 0; i < series.size(); i++) {
                StockPriceDaily current = series.get(i);
                if (!current.getTradeDate().equals(tradeDate)) {
                    continue;
                }

                List<StockPriceDaily> history = series.subList(0, i + 1);
                StockIndicatorDaily indicator = stockIndicatorDailyRepository.findByStockCodeAndTradeDate(current.getStockCode(), tradeDate)
                        .orElseGet(StockIndicatorDaily::new);
                indicator.setStockCode(current.getStockCode());
                indicator.setTradeDate(tradeDate);
                indicator.setMa5(calcCloseAverage(history, 5));
                indicator.setMa10(calcCloseAverage(history, 10));
                indicator.setMa20(calcCloseAverage(history, 20));
                indicator.setMa60(calcCloseAverage(history, 60));
                indicator.setAvgVol5(calcVolumeAverage(history, 5));
                indicator.setAvgVol20(calcVolumeAverage(history, 20));
                indicator.setPctChange(calcPctChange(series, i));
                stockIndicatorDailyRepository.save(indicator);
                count++;
            }
        }
        return count;
    }

    private BigDecimal calcCloseAverage(List<StockPriceDaily> history, int period) {
        int start = Math.max(0, history.size() - period);
        List<BigDecimal> closes = history.subList(start, history.size()).stream()
                .map(StockPriceDaily::getClosePrice)
                .collect(Collectors.toList());
        return BigDecimalUtils.average(closes, 4);
    }

    private BigDecimal calcVolumeAverage(List<StockPriceDaily> history, int period) {
        int start = Math.max(0, history.size() - period);
        List<BigDecimal> volumes = new ArrayList<>();
        for (StockPriceDaily price : history.subList(start, history.size())) {
            volumes.add(BigDecimal.valueOf(price.getVolume()));
        }
        return BigDecimalUtils.average(volumes, 4);
    }

    private BigDecimal calcPctChange(List<StockPriceDaily> series, int currentIndex) {
        if (currentIndex == 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
        BigDecimal currentClose = series.get(currentIndex).getClosePrice();
        BigDecimal previousClose = series.get(currentIndex - 1).getClosePrice();
        return BigDecimalUtils.percentageChange(currentClose, previousClose, 4);
    }
}

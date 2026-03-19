package tool.stock.v8.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8.entity.StockPriceDaily;
import tool.stock.v8.entity.StrategyBacktestResult;
import tool.stock.v8.entity.StrategySignal;
import tool.stock.v8.repository.StockPriceDailyRepository;
import tool.stock.v8.repository.StrategyBacktestResultRepository;
import tool.stock.v8.repository.StrategySignalRepository;
import tool.stock.v8.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 回測隔日、3日、5日績效。
 */
@Service
public class BacktestService {

    private final BreakoutStrategyService breakoutStrategyService;
    private final StrategySignalRepository strategySignalRepository;
    private final StockPriceDailyRepository stockPriceDailyRepository;
    private final StrategyBacktestResultRepository strategyBacktestResultRepository;

    public BacktestService(BreakoutStrategyService breakoutStrategyService,
                           StrategySignalRepository strategySignalRepository,
                           StockPriceDailyRepository stockPriceDailyRepository,
                           StrategyBacktestResultRepository strategyBacktestResultRepository) {
        this.breakoutStrategyService = breakoutStrategyService;
        this.strategySignalRepository = strategySignalRepository;
        this.stockPriceDailyRepository = stockPriceDailyRepository;
        this.strategyBacktestResultRepository = strategyBacktestResultRepository;
    }

    @Transactional
    public int backtest(LocalDate startDate, LocalDate endDate) {
        List<StrategySignal> signals = strategySignalRepository.findByStrategyCodeAndTradeDateBetweenOrderByTradeDateAscStockCodeAsc(
                breakoutStrategyService.getStrategyCode(), startDate, endDate);
        int count = 0;
        for (StrategySignal signal : signals) {
            List<StockPriceDaily> series = new ArrayList<>(stockPriceDailyRepository.findByStockCodeOrderByTradeDateAsc(signal.getStockCode()));
            series.sort(Comparator.comparing(StockPriceDaily::getTradeDate));
            int signalIndex = findIndex(series, signal.getTradeDate());
            if (signalIndex < 0) {
                continue;
            }

            StrategyBacktestResult result = strategyBacktestResultRepository
                    .findByStrategyCodeAndStockCodeAndSignalDate(breakoutStrategyService.getStrategyCode(), signal.getStockCode(), signal.getTradeDate())
                    .orElseGet(StrategyBacktestResult::new);
            result.setStrategyCode(breakoutStrategyService.getStrategyCode());
            result.setStockCode(signal.getStockCode());
            result.setStockName(signal.getStockName());
            result.setSignalDate(signal.getTradeDate());
            result.setEntryPrice(signal.getSignalPrice());

            fillReturn(result, series, signalIndex, 1);
            fillReturn(result, series, signalIndex, 3);
            fillReturn(result, series, signalIndex, 5);

            strategyBacktestResultRepository.save(result);
            count++;
        }
        return count;
    }

    public List<StrategyBacktestResult> getResults(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return strategyBacktestResultRepository.findByStrategyCodeAndSignalDateBetweenOrderBySignalDateAscStockCodeAsc(
                    breakoutStrategyService.getStrategyCode(), startDate, endDate);
        }
        return strategyBacktestResultRepository.findByStrategyCodeOrderBySignalDateAscStockCodeAsc(breakoutStrategyService.getStrategyCode());
    }

    private int findIndex(List<StockPriceDaily> series, LocalDate tradeDate) {
        for (int i = 0; i < series.size(); i++) {
            if (series.get(i).getTradeDate().equals(tradeDate)) {
                return i;
            }
        }
        return -1;
    }

    private void fillReturn(StrategyBacktestResult result, List<StockPriceDaily> series, int signalIndex, int offset) {
        StockPriceDaily target = signalIndex + offset < series.size() ? series.get(signalIndex + offset) : null;
        BigDecimal closePrice = target == null ? null : target.getClosePrice();
        BigDecimal ratioReturn = BigDecimalUtils.ratioReturn(closePrice, result.getEntryPrice(), 6);
        int hit = ratioReturn != null && ratioReturn.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0;

        if (offset == 1) {
            result.setNextDayClose(closePrice);
            result.setReturn1d(ratioReturn);
            result.setHit1d(hit);
        } else if (offset == 3) {
            result.setDay3Close(closePrice);
            result.setReturn3d(ratioReturn);
            result.setHit3d(hit);
        } else if (offset == 5) {
            result.setDay5Close(closePrice);
            result.setReturn5d(ratioReturn);
            result.setHit5d(hit);
        }
    }
}

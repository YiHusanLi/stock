package tool.stock.v8.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8.dto.SignalQueryResponse;
import tool.stock.v8.dto.StrategyEvaluationResult;
import tool.stock.v8.entity.StockIndicatorDaily;
import tool.stock.v8.entity.StockPriceDaily;
import tool.stock.v8.entity.StrategySignal;
import tool.stock.v8.repository.StockIndicatorDailyRepository;
import tool.stock.v8.repository.StockPriceDailyRepository;
import tool.stock.v8.repository.StrategySignalRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 掃描指定日期所有股票，寫入策略訊號。
 */
@Service
public class StrategyScanService {

    private final BreakoutStrategyService breakoutStrategyService;
    private final StockPriceDailyRepository stockPriceDailyRepository;
    private final StockIndicatorDailyRepository stockIndicatorDailyRepository;
    private final StrategySignalRepository strategySignalRepository;

    public StrategyScanService(BreakoutStrategyService breakoutStrategyService,
                               StockPriceDailyRepository stockPriceDailyRepository,
                               StockIndicatorDailyRepository stockIndicatorDailyRepository,
                               StrategySignalRepository strategySignalRepository) {
        this.breakoutStrategyService = breakoutStrategyService;
        this.stockPriceDailyRepository = stockPriceDailyRepository;
        this.stockIndicatorDailyRepository = stockIndicatorDailyRepository;
        this.strategySignalRepository = strategySignalRepository;
    }

    @Transactional
    public int scan(LocalDate tradeDate) {
        List<StockPriceDaily> prices = stockPriceDailyRepository.findByTradeDateOrderByStockCodeAsc(tradeDate);
        strategySignalRepository.deleteByStrategyCodeAndTradeDate(breakoutStrategyService.getStrategyCode(), tradeDate);

        int count = 0;
        for (StockPriceDaily todayPrice : prices) {
            Optional<StockIndicatorDaily> indicatorOptional = stockIndicatorDailyRepository.findByStockCodeAndTradeDate(todayPrice.getStockCode(), tradeDate);
            if (indicatorOptional.isEmpty()) {
                continue;
            }
            List<StockPriceDaily> history = stockPriceDailyRepository.findByStockCodeOrderByTradeDateAsc(todayPrice.getStockCode());
            StrategyEvaluationResult evaluation = breakoutStrategyService.evaluate(todayPrice, indicatorOptional.get(), history, tradeDate);
            if (!evaluation.isMatched()) {
                continue;
            }

            StrategySignal signal = new StrategySignal();
            signal.setStrategyCode(breakoutStrategyService.getStrategyCode());
            signal.setStockCode(todayPrice.getStockCode());
            signal.setStockName(todayPrice.getStockName());
            signal.setTradeDate(tradeDate);
            signal.setSignalPrice(todayPrice.getClosePrice());
            signal.setPctChange(indicatorOptional.get().getPctChange());
            signal.setVolume(todayPrice.getVolume());
            signal.setMa5(indicatorOptional.get().getMa5());
            signal.setMa10(indicatorOptional.get().getMa10());
            signal.setMa20(indicatorOptional.get().getMa20());
            signal.setSqueezeBurstFlag(evaluation.isSqueezeBurst());
            signal.setMultiMaBreakFlag(evaluation.isMultiMaBreak());
            signal.setTrendBreakFlag(evaluation.isTrendBreak());
            signal.setTrendSlope(evaluation.getTrendSlope());
            signal.setTrendlineValue(evaluation.getTrendlineValue());
            signal.setRemark(evaluation.getRemark());
            strategySignalRepository.save(signal);
            count++;
        }
        return count;
    }

    public List<SignalQueryResponse> getSignals(LocalDate tradeDate) {
        List<StrategySignal> signals = strategySignalRepository.findByStrategyCodeAndTradeDateOrderByStockCodeAsc(
                breakoutStrategyService.getStrategyCode(), tradeDate);
        List<SignalQueryResponse> result = new ArrayList<>();
        for (StrategySignal signal : signals) {
            SignalQueryResponse dto = new SignalQueryResponse();
            dto.setStockCode(signal.getStockCode());
            dto.setStockName(signal.getStockName());
            dto.setTradeDate(signal.getTradeDate());
            dto.setClosePrice(signal.getSignalPrice());
            dto.setPctChange(signal.getPctChange());
            dto.setVolume(signal.getVolume());
            dto.setMa5(signal.getMa5());
            dto.setMa10(signal.getMa10());
            dto.setMa20(signal.getMa20());
            dto.setSqueezeBurst(signal.isSqueezeBurstFlag());
            dto.setMultiMaBreak(signal.isMultiMaBreakFlag());
            dto.setTrendBreak(signal.isTrendBreakFlag());
            dto.setRemark(signal.getRemark());
            result.add(dto);
        }
        return result;
    }
}

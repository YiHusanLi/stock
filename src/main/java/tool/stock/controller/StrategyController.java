package tool.stock.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.dto.ApiResponse;
import tool.stock.dto.BacktestRequest;
import tool.stock.dto.IndicatorCalculateRequest;
import tool.stock.dto.SignalQueryResponse;
import tool.stock.dto.TwseDailyImportRequest;
import tool.stock.entity.StrategyBacktestResult;
import tool.stock.service.BacktestService;
import tool.stock.service.IndicatorCalculateService;
import tool.stock.service.StrategyScanService;
import tool.stock.service.TwseImportService;

import java.time.LocalDate;
import java.util.List;

/**
 * V8_BREAKOUT API。
 */
@RestController
@RequestMapping("/api")
public class StrategyController {

    private final TwseImportService twseImportService;
    private final IndicatorCalculateService indicatorCalculateService;
    private final StrategyScanService strategyScanService;
    private final BacktestService backtestService;

    public StrategyController(TwseImportService twseImportService,
                              IndicatorCalculateService indicatorCalculateService,
                              StrategyScanService strategyScanService,
                              BacktestService backtestService) {
        this.twseImportService = twseImportService;
        this.indicatorCalculateService = indicatorCalculateService;
        this.strategyScanService = strategyScanService;
        this.backtestService = backtestService;
    }

    @PostMapping("/import/twse/daily")
    public ApiResponse<Integer> importDaily(@RequestBody TwseDailyImportRequest request) {
        return ApiResponse.success(twseImportService.importDailyPrices(request.getTradeDate()));
    }

    @PostMapping("/indicator/calculate")
    public ApiResponse<Integer> calculateIndicator(@RequestBody IndicatorCalculateRequest request) {
        return ApiResponse.success(indicatorCalculateService.calculate(request.getTradeDate()));
    }

    @PostMapping("/strategy/v8/scan")
    public ApiResponse<Integer> scan(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(strategyScanService.scan(date));
    }

    @GetMapping("/strategy/v8/signals")
    public ApiResponse<List<SignalQueryResponse>> signals(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(strategyScanService.getSignals(date));
    }

    @PostMapping("/strategy/v8/backtest")
    public ApiResponse<Integer> backtest(@RequestBody BacktestRequest request) {
        return ApiResponse.success(backtestService.backtest(request.getStartDate(), request.getEndDate()));
    }

    @GetMapping("/strategy/v8/backtest/result")
    public ApiResponse<List<StrategyBacktestResult>> backtestResult(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.success(backtestService.getResults(startDate, endDate));
    }
}

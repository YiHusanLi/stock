package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.v8sniper.config.V8Properties;
import tool.stock.v8sniper.dto.RunSummary;
import tool.stock.v8sniper.dto.StockSnapshot;
import tool.stock.v8sniper.dto.StrategyPickView;
import tool.stock.v8sniper.dto.StrategyResultBundle;
import tool.stock.v8sniper.entity.StockBasicEntity;
import tool.stock.v8sniper.entity.StockDailyQuoteEntity;
import tool.stock.v8sniper.entity.StockIndicatorDailyEntity;
import tool.stock.v8sniper.repository.StockBasicRepository;
import tool.stock.v8sniper.repository.StockDailyQuoteRepository;
import tool.stock.v8sniper.repository.StockIndicatorDailyRepository;
import tool.stock.v8sniper.repository.StrategyPickDetailRepository;
import tool.stock.v8sniper.repository.StrategyPickResultRepository;
import tool.stock.v8sniper.strategy.StrategyEngine;
import tool.stock.v8sniper.util.CsvExportUtil;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyRunService {
    private final IndicatorService indicatorService;
    private final StrategyEngine strategyEngine;
    private final StockDailyQuoteRepository quoteRepository;
    private final StockIndicatorDailyRepository indicatorRepository;
    private final StockBasicRepository basicRepository;
    private final StrategyPickResultRepository resultRepository;
    private final StrategyPickDetailRepository detailRepository;
    private final StrategyQueryService queryService;
    private final FileStorageService fileStorageService;
    private final V8Properties properties;

    public StrategyRunService(IndicatorService indicatorService, StrategyEngine strategyEngine, StockDailyQuoteRepository quoteRepository,
                              StockIndicatorDailyRepository indicatorRepository, StockBasicRepository basicRepository,
                              StrategyPickResultRepository resultRepository, StrategyPickDetailRepository detailRepository,
                              StrategyQueryService queryService, FileStorageService fileStorageService, V8Properties properties) {
        this.indicatorService = indicatorService; this.strategyEngine = strategyEngine; this.quoteRepository = quoteRepository;
        this.indicatorRepository = indicatorRepository; this.basicRepository = basicRepository; this.resultRepository = resultRepository;
        this.detailRepository = detailRepository; this.queryService = queryService; this.fileStorageService = fileStorageService; this.properties = properties;
    }

    @Transactional
    public RunSummary run(LocalDate date) throws Exception {
        int indicatorCount = indicatorService.rebuild(date);
        resultRepository.deleteByTradeDateAndStrategyName(date, properties.getStrategy().getV8().getStrategyName());
        detailRepository.deleteByTradeDateAndStrategyName(date, properties.getStrategy().getV8().getStrategyName());
        List<StockDailyQuoteEntity> todayRows = quoteRepository.findByTradeDateOrderByStockCodeAsc(date);
        int picked = 0;
        for (StockDailyQuoteEntity today : todayRows) {
            StockIndicatorDailyEntity indicator = indicatorRepository.findByTradeDateAndStockCode(date, today.getStockCode()).orElse(null);
            if (indicator == null) continue;
            List<StockDailyQuoteEntity> history = quoteRepository.findByStockCodeOrderByTradeDateAsc(today.getStockCode());
            StockBasicEntity basic = basicRepository.findByStockCode(today.getStockCode()).orElse(null);
            boolean previous = resultRepository.findByTradeDateAndStockCodeAndStrategyName(date.minusDays(1), today.getStockCode(), properties.getStrategy().getV8().getStrategyName()).isPresent();
            StrategyResultBundle bundle = strategyEngine.runV8(new StockSnapshot(basic, today, indicator, history, previous));
            detailRepository.saveAll(bundle.getDetails());
            if (bundle.isPicked()) { resultRepository.save(bundle.getResult()); picked++; }
        }
        List<StrategyPickView> rows = queryService.findByDate(date);
        Path exportPath = CsvExportUtil.writeResults(fileStorageService.exportFile(date, "v8_pick_result.csv"), rows);
        List<String> messages = new ArrayList<>();
        messages.add("已重建指標筆數: " + indicatorCount);
        messages.add("已匯出 CSV: " + exportPath.toAbsolutePath());
        return new RunSummary(picked, exportPath.toAbsolutePath().toString(), messages);
    }
}

package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import tool.stock.v8sniper.dto.StrategyPickView;
import tool.stock.v8sniper.entity.StrategyPickResultEntity;
import tool.stock.v8sniper.repository.StrategyPickResultRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StrategyQueryService {
    private final StrategyPickResultRepository repository;
    public StrategyQueryService(StrategyPickResultRepository repository) { this.repository = repository; }
    public List<StrategyPickView> findByDate(LocalDate date) { return repository.findByTradeDateOrderByTotalScoreDescStockCodeAsc(date).stream().map(this::toView).collect(Collectors.toList()); }
    public List<StrategyPickView> history(String stockCode) { return repository.findByStockCodeOrderByTradeDateDesc(stockCode).stream().map(this::toView).collect(Collectors.toList()); }
    private StrategyPickView toView(StrategyPickResultEntity e) {
        StrategyPickView v = new StrategyPickView();
        v.setTradeDate(e.getTradeDate()); v.setStockCode(e.getStockCode()); v.setStockName(e.getStockName()); v.setIndustry(e.getIndustry());
        v.setTotalScore(e.getTotalScore()); v.setClosePrice(e.getClosePrice()); v.setPctChange(e.getPctChange()); v.setVolume(e.getVolume());
        v.setVolumeRatio(e.getVolumeRatio()); v.setTrendSlope(e.getTrendSlope()); v.setSuggestedBuyPrice(e.getSuggestedBuyPrice()); v.setPickReason(e.getPickReason()); v.setRating(e.getRating());
        return v;
    }
}

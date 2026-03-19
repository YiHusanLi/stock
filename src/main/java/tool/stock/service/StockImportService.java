package tool.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool.stock.mapper.StockMapper;
import tool.stock.repository.RevenueMonthlyRepository;
import tool.stock.repository.StockBasicRepository;
import tool.stock.repository.StockDailyRepository;
import tool.stock.repository.StockValuationRepository;

@Service
public class StockImportService {

    private final TwseClientService twseClientService;
    private final StockMapper stockMapper;
    private final StockBasicRepository stockBasicRepository;
    private final StockDailyRepository stockDailyRepository;
    private final StockValuationRepository stockValuationRepository;
    private final RevenueMonthlyRepository revenueMonthlyRepository;

    public StockImportService(
            TwseClientService twseClientService,
            StockMapper stockMapper,
            StockBasicRepository stockBasicRepository,
            StockDailyRepository stockDailyRepository,
            StockValuationRepository stockValuationRepository,
            RevenueMonthlyRepository revenueMonthlyRepository
    ) {
        this.twseClientService = twseClientService;
        this.stockMapper = stockMapper;
        this.stockBasicRepository = stockBasicRepository;
        this.stockDailyRepository = stockDailyRepository;
        this.stockValuationRepository = stockValuationRepository;
        this.revenueMonthlyRepository = revenueMonthlyRepository;
    }

    @Transactional
    public void importAll() {
        importCompanyBasic();
        importStockDaily();
        importValuation();
        importRevenueMonthly();
    }

    public void importCompanyBasic() {
        twseClientService.getCompanyBasic()
                .stream()
                .map(stockMapper::toStockBasic)
                .forEach(stockBasicRepository::save);
    }

    public void importStockDaily() {
        twseClientService.getStockDayAll()
                .stream()
                .map(stockMapper::toStockDaily)
                .forEach(stockDailyRepository::save);
    }

    public void importValuation() {
        twseClientService.getBwibbuAll()
                .stream()
                .map(stockMapper::toStockValuation)
                .forEach(stockValuationRepository::save);
    }

    public void importRevenueMonthly() {
        twseClientService.getRevenueMonthly()
                .stream()
                .map(stockMapper::toRevenueMonthly)
                .forEach(revenueMonthlyRepository::save);
    }
}
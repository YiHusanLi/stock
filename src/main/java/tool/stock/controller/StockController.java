package tool.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.service.StockAnalysisService;

import java.util.List;
import java.util.Map;

@RestController
public class StockController {

    private final StockAnalysisService service;

    public StockController(StockAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/api/stocks/top")
    public List<Map<String, Object>> topStocks() {
        return service.analyzeTopStocks();
    }
}
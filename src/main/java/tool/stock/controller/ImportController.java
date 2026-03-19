package tool.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.service.StockImportService;

@RestController
public class ImportController {

    private final StockImportService stockImportService;

    public ImportController(StockImportService stockImportService) {
        this.stockImportService = stockImportService;
    }

    @GetMapping("/api/import/stocks")
    public String importStocks() {
        stockImportService.importAll();
        return "OK";
    }
}
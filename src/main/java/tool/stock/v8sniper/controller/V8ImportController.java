package tool.stock.v8sniper.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.v8sniper.dto.ApiResponse;
import tool.stock.v8sniper.dto.ImportSummary;
import tool.stock.v8sniper.service.TwseImportFacadeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/import/twse")
public class V8ImportController {
    private final TwseImportFacadeService importService;
    public V8ImportController(TwseImportFacadeService importService) { this.importService = importService; }
    @GetMapping("/daily")
    public ApiResponse<List<ImportSummary>> daily(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(importService.importDaily(date));
    }
    @GetMapping("/range")
    public ApiResponse<List<ImportSummary>> range(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                  @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ApiResponse.ok(importService.importRange(start, end));
    }
}

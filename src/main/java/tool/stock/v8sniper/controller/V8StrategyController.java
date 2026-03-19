package tool.stock.v8sniper.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.v8sniper.dto.ApiResponse;
import tool.stock.v8sniper.dto.RunSummary;
import tool.stock.v8sniper.dto.StrategyPickView;
import tool.stock.v8sniper.service.StrategyQueryService;
import tool.stock.v8sniper.service.StrategyRunService;

import java.time.LocalDate;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/strategy")
public class V8StrategyController {
    private final StrategyRunService runService;
    private final StrategyQueryService queryService;
    public V8StrategyController(StrategyRunService runService, StrategyQueryService queryService) { this.runService = runService; this.queryService = queryService; }
    @GetMapping("/run")
    public ApiResponse<RunSummary> run(@RequestParam("v") int v, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        if (v != 8) { return ApiResponse.fail("目前僅支援 v=8"); }
        return ApiResponse.ok(runService.run(date));
    }
    @GetMapping("/results")
    public ApiResponse<List<StrategyPickView>> results(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(queryService.findByDate(date));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        java.nio.file.Path path = java.nio.file.Path.of("data", "export", date.format(DateTimeFormatter.BASIC_ISO_DATE), "v8_pick_result.csv");
        byte[] bytes = Files.readAllBytes(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=v8_pick_result.csv")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(bytes);
    }
    @GetMapping("/history")
    public ApiResponse<List<StrategyPickView>> history(@RequestParam("stockCode") String stockCode) {
        return ApiResponse.ok(queryService.history(stockCode));
    }
}

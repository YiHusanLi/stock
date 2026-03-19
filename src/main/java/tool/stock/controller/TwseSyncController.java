package tool.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tool.stock.service.TwseSyncService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class TwseSyncController {

    private final TwseSyncService twseSyncService;

    public TwseSyncController(TwseSyncService twseSyncService) {
        this.twseSyncService = twseSyncService;
    }

    @GetMapping("/admin/twse/sync")
    public Map<String, Object> sync() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            result.put("success", true);
            result.put("data", twseSyncService.syncAll());
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getClass().getName());
            result.put("message", e.getMessage());
            return result;
        }
    }
}
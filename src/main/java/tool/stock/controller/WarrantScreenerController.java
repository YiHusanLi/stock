package tool.stock.controller;

import tool.stock.dto.WarrantSearchRequest;
import tool.stock.dto.WarrantSearchResponse;
import tool.stock.service.WarrantScreenerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screener/warrants")
@CrossOrigin
public class WarrantScreenerController {

    private final WarrantScreenerService warrantScreenerService;

    public WarrantScreenerController(WarrantScreenerService warrantScreenerService) {
        this.warrantScreenerService = warrantScreenerService;
    }

    @PostMapping("/search")
    public List<WarrantSearchResponse> search(@RequestBody WarrantSearchRequest request) {
        return warrantScreenerService.search(request);
    }
}
package tool.stock.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tool.stock.dto.twse.BwibbuAllDto;
import tool.stock.dto.twse.CompanyBasicDto;
import tool.stock.dto.twse.NoticeDto;
import tool.stock.dto.twse.RevenueMonthlyDto;
import tool.stock.dto.twse.StockDayAllDto;
import tool.stock.dto.twse.WarrantBasicDto;

import java.util.Collections;
import java.util.List;

@Service
public class TwseClientService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "https://openapi.twse.com.tw/v1";

    public TwseClientService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<StockDayAllDto> getStockDayAll() {
        return getList("/exchangeReport/STOCK_DAY_ALL", new TypeReference<List<StockDayAllDto>>() {});
    }

    public List<BwibbuAllDto> getBwibbuAll() {
        return getList("/exchangeReport/BWIBBU_ALL", new TypeReference<List<BwibbuAllDto>>() {});
    }

    public List<CompanyBasicDto> getCompanyBasic() {
        return getList("/opendata/t187ap03_L", new TypeReference<List<CompanyBasicDto>>() {});
    }

    public List<RevenueMonthlyDto> getRevenueMonthly() {
        return getList("/opendata/t187ap05_L", new TypeReference<List<RevenueMonthlyDto>>() {});
    }

    public List<NoticeDto> getNotice() {
        return getList("/announcement/notice", new TypeReference<List<NoticeDto>>() {});
    }

    public List<WarrantBasicDto> getWarrantBasic() {
        return getList("/opendata/t187ap37_L", new TypeReference<List<WarrantBasicDto>>() {});
    }

    private <T> List<T> getList(String path, TypeReference<List<T>> typeReference) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + path, String.class);
            String body = response.getBody();

            if (body == null || body.isBlank()) {
                return Collections.emptyList();
            }

            String trimmed = body.trim();
            if (trimmed.startsWith("<")) {
                throw new IllegalStateException("TWSE API 回傳 HTML，不是 JSON，path=" + path);
            }

            return objectMapper.readValue(trimmed, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("呼叫 TWSE API 失敗: " + path, e);
        }
    }
}
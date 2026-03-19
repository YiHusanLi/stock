package tool.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 通用 TWSE API Client，集中處理 HTTP 與 JSON 解析。
 */
@Component
public class TwseApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.twse.base-url}")
    private String twseBaseUrl;

    public TwseApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode getArray(String apiPath) {
        ResponseEntity<String> response = restTemplate.getForEntity(twseBaseUrl + apiPath, String.class);
        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("TWSE API 回傳空白內容: " + apiPath);
        }
        try {
            JsonNode root = objectMapper.readTree(body);
            if (!root.isArray()) {
                throw new IllegalStateException("TWSE API 回傳不是 JSON Array: " + apiPath);
            }
            return root;
        } catch (Exception e) {
            throw new IllegalStateException("TWSE API 解析失敗: " + apiPath, e);
        }
    }
}

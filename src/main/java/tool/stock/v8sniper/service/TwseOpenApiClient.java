package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tool.stock.v8sniper.config.V8Properties;

@Service
public class TwseOpenApiClient {
    private final RestTemplate restTemplate;
    private final V8Properties properties;
    public TwseOpenApiClient(RestTemplate restTemplate, V8Properties properties) { this.restTemplate = restTemplate; this.properties = properties; }
    public String fetch(String endpoint) {
        return restTemplate.getForObject(properties.getTwse().getBaseUrl() + endpoint, String.class);
    }
}

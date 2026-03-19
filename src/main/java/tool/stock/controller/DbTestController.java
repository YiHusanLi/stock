package tool.stock.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DbTestController {

    private final JdbcTemplate jdbcTemplate;

    public DbTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/admin/db/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new LinkedHashMap<>();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM import_batch", Integer.class);

        result.put("success", true);
        result.put("import_batch_count", count);
        return result;
    }
}
package tool.stock.v8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 簡單首頁。
 */
@Controller
public class PageController {

    @GetMapping("/legacy-v8")
    public String index() {
        return "index";
    }
}

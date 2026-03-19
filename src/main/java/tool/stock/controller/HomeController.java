package tool.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首頁控制器。
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

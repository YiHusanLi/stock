package tool.stock.v8sniper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class V8PageController {
    @GetMapping("/")
    public String index() { return "index"; }
}

package org.geekhub.doctorsregistry.web.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class IndexController {

    @GetMapping({"", "/", "/index"})
    public String index() {
        return "index";
    }
}

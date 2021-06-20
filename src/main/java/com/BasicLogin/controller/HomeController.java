package com.BasicLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@CrossOrigin
public class HomeController {

    @GetMapping("/")
    public String getSwaggerPage() {
        return "redirect:/swagger-ui.html";
    }
}

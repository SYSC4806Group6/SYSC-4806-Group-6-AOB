package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // This just returns the name of the Thymeleaf template
        return "login";
    }
}
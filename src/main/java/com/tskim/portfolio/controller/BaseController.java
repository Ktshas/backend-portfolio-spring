package com.tskim.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    
    @GetMapping("/")
    public String home() {
        return "Welcome to Tskim Portfolio Backend!";
    }
    
    @GetMapping("/api/test")
    public String test() {
        return "API is working!";
    }
}

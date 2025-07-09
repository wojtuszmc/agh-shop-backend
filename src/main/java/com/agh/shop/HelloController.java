package com.agh.shop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "AGH Shop Backend is running! 🚀";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from AGH Shop API!";
    }
}
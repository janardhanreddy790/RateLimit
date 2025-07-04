package com.nagam.example.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello endpoint";
    }

    @GetMapping("/status")
    public String status() {
        return "Status endpoint";
    }

    @GetMapping("/health")
    public String health() {
        return "Health check endpoint";
    }

    @GetMapping("/data")
    public String data() {
        return "Data endpoint";
    }
}

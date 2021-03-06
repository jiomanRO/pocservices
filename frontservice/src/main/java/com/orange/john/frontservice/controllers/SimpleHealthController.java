package com.orange.john.frontservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleHealthController {

    @RequestMapping("/simpleHealth")
    public String simpleHealth() {
        return "ALLOK";
    }

    @GetMapping("/health")
    public String health() {
        return "HEALTHY";
    }
}

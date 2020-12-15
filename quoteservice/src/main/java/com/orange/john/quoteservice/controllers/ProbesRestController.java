package com.orange.john.quoteservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProbesRestController {

    @GetMapping("/liveness")
    public String alive() {
        return "ok";
    }
}

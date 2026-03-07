package com.maplewood.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health", description = "Checks Health for the API")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
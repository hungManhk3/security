package com.example.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/ping")
    public Map<String, Boolean> ping() {
        return Map.of("pong", true);
    }
}

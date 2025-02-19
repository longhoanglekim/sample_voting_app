package com.example.worker.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {
    @GetMapping
    public String result(Model model) {
        log.info("showing result");
        return "index";
    }
}
package com.example.worker.controllers;

import com.example.worker.services.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/result")
@Slf4j
public class ResultController {
    @Autowired
    DatabaseService databaseService;
    @GetMapping
    public String result(Model model) {
        log.info("showing result");
        Map<String, Integer> voteMap = databaseService.getCountVotes();
//        int dogVotes = voteMap.get("Dogs") / (voteMap.get("Dogs") + voteMap.get("Cats")) * 100 ;
        model.addAttribute("dogs", 10);
        model.addAttribute("cats", 100 - 10);
        return "index";
    }
}

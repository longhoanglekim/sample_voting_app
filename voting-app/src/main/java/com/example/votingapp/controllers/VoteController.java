package com.example.votingapp.controllers;

import com.example.votingapp.services.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vote")
@Slf4j
public class VoteController {

    @Autowired
    private RedisService redisService;

    private static final String OPTION_A = "Cats";
    private static final String OPTION_B = "Dogs";

    @GetMapping("/")
    public String showVotingPage(HttpServletRequest request, Model model) {
        log.info("Showing voting page");
        String voterId = getVoterId(request);
        model.addAttribute("optionA", OPTION_A);
        model.addAttribute("optionB", OPTION_B);
        model.addAttribute("voterId", voterId);
        return "index";
    }

    @PostMapping("/")
    public String submitVote(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam String vote, Model model) {
        String voterId = getVoterId(request);
        redisService.saveVote(voterId, vote);
        log.info("Vote submitted: " + vote);
        model.addAttribute("optionA", OPTION_A);
        model.addAttribute("optionB", OPTION_B);
        model.addAttribute("voterId", voterId);
        if (vote.equals("optionA")) {
            model.addAttribute("vote", OPTION_A);
        } else if (vote.equals("optionB")) {
            model.addAttribute("vote", OPTION_B);
        }
        return "index";
    }

    private String getVoterId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("voter_id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return redisService.generateVoterId();
    }
}
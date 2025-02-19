package com.example.votingapp.services;

import com.example.votingapp.models.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Lưu vote vào Redis
    public void saveVote(String voterId, String choice) {
        try {
            Vote vote = new Vote();
            vote.setVoterId(voterId);
            vote.setChoice(choice);
            String jsonVote = objectMapper.writeValueAsString(vote);
            log.info("save vote: " + jsonVote);
            redisTemplate.opsForList().rightPush("votes", jsonVote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo voter_id nếu chưa có
    public String generateVoterId() {
        return Long.toHexString(new Random().nextLong());
    }
}
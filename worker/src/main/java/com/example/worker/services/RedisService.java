package com.example.worker.services;

import com.example.worker.models.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class RedisService {

    private JedisPool jedisPool;  // Sử dụng JedisPool
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisService() {
        connectRedis();
    }

    // Kết nối Redis thông qua JedisPool
    private void connectRedis() {
        while (true) {
            try {
                jedisPool = new JedisPool("localhost", 6379); // Cấu hình địa chỉ Redis
                log.info("✅ Connected to Redis");
                break;
            } catch (Exception e) {
                log.error("⏳ Waiting for Redis...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    // Lấy danh sách phiếu bầu từ Redis
    public List<String> popVotes() {
        try (Jedis jedis = jedisPool.getResource()) {  // Lấy kết nối từ JedisPool
            List<String> jsonList = jedis.lrange("votes", 0, -1);  // Lấy toàn bộ danh sách "votes"
            if (jsonList != null && !jsonList.isEmpty()) {
                List<String> choices = new ArrayList<>();
                for (String json : jsonList) {
                    Vote vote = objectMapper.readValue(json, Vote.class);  // Deserialize JSON thành đối tượng Vote
                    choices.add(vote.getChoice());  // Thêm lựa chọn vào danh sách
                }
                return choices;  // Trả về danh sách chứa tất cả các lựa chọn
            }
        } catch (Exception e) {
            log.error("❌ Redis error: " + e.getMessage());
            connectRedis();  // Hàm kết nối lại Redis
        }
        return Collections.emptyList();
    }

}

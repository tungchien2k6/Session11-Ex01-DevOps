package com.ra.pharmacyservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.pharmacyservice.dto.AlertRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(AlertRequest request) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(request);
            redisTemplate.convertAndSend(request.getTopicListen(), jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
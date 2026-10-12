package com.ra.pharmacyservice.controller;

import com.ra.pharmacyservice.dto.AlertRequest;
import com.ra.pharmacyservice.service.RedisPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final RedisPublisher redisPublisher;

    public AlertController(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @PostMapping
    public ResponseEntity<String> sendAlert(@RequestBody AlertRequest request) {
        redisPublisher.publish(request);
        return ResponseEntity.ok("Đã gửi thông báo đến kênh : " + request.getTopicListen());
    }
}
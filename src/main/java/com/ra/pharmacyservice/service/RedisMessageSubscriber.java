package com.ra.pharmacyservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.pharmacyservice.dto.AlertRequest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = new String(message.getBody());
            AlertRequest alert = objectMapper.readValue(jsonMessage, AlertRequest.class);

            System.out.println("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===");
            System.out.println("Type : " + alert.getType());
            System.out.println("Nội dung: " + alert.getMessage());
            System.out.println("=================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
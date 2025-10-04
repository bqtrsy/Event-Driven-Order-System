package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.orderservice.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOrder(Order order) {
        try {
            String orderJson = new ObjectMapper().writeValueAsString(order);
            rabbitTemplate.convertAndSend("order.exchange", "order.created", orderJson);
            System.out.println("✅ Published Order Event: " + orderJson);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order to JSON", e);
        }
    }
}

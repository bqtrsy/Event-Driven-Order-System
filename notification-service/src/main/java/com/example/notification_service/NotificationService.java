package com.example.notification_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "order-notification-queue")
    public void handleOrderEvent(String orderJson) {
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            log.info("📣 Sending notification for order {} with status {}", order.getId(), order.getStatus());

            // Send email / SMS logic (simulated)
            Thread.sleep(500);

        } catch (Exception e) {
            log.error("❌ Notification failed", e);
        }
    }
}


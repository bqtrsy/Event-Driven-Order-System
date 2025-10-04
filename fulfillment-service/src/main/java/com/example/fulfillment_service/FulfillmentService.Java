package com.example.fulfillment_service;

import com.example.orderservice.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FulfillmentService {

    private static final Logger log = LoggerFactory.getLogger(FulfillmentService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // This method subscribes/listens to the "order.paid" queue
    @RabbitListener(queues = "order.paid-queue")
    public void handleOrderPaid(String orderJson) {
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            log.info("📦 Fulfillment processing for order: {}", order.getId());

            // Simulate processing
            Thread.sleep(1000);
            log.info("✅ Order {} fulfilled", order.getId());

        } catch (Exception e) {
            log.error("❌ Fulfillment failed", e);
        }
    }
}

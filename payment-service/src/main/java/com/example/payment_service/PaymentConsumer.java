package com.example.payment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    // Listen for messages from the queue
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderEvent(String orderJson) {
        try {
            log.info("📩 Received Order Event: {}", orderJson);

            // Convert JSON to Order object
            Order order = objectMapper.readValue(orderJson, Order.class);

            // Simulate payment
            boolean paymentSuccess = processPayment(order);
            String status = paymentSuccess ? "PAID" : "FAILED";

            // Call Order Service to update status
            String url = "http://localhost:8081/orders/" + order.getId() + "?status=" + status;
            restTemplate.patchForObject(url, null, String.class);

            log.info("💳 Payment processed for order {} → {}", order.getId(), status);
        } catch (Exception e) {
            log.error("❌ Error handling order event", e);
        }
    }

    private boolean processPayment(Order order) {
        return Math.random() > 0.2; // 80% success
    }
}

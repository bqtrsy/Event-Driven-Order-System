package com.example.payment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderEvent(String orderJson) {
        try {
            log.info("📩 Received Order Event: {}", orderJson);

            // Convert JSON string to Order object
            Order order = objectMapper.readValue(orderJson, Order.class);

            // Simulate payment processing
            boolean paymentSuccess = processPayment(order);
            String status = paymentSuccess ? "PAID" : "FAILED";

            // Update order status back to Order Service
            String url = "http://localhost:8081/orders/" + order.getId() + "?status=" + status;
            try {
                String response = restTemplate.patchForObject(url, null, String.class);
                log.info("✅ PATCH success for order {} → {} | Response: {}", order.getId(), status, response);
            } catch (Exception ex) {
                log.error("❌ PATCH failed for order {} → {} | URL: {}", order.getId(), status, url, ex);
            }
        } catch (Exception e) {
            log.error("❌ Failed to process order event", e);
        }
    }

    private boolean processPayment(Order order) {
        // Simulate random payment success/failure (80% chance success)
        return Math.random() > 0.2;
    }

    @RabbitListener(queues = "payment.queue")
    public void consume(OrderEvent event) {
        System.out.println("📩 Received order event: " + event);
    }
}

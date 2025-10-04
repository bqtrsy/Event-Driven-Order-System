package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderRepository repository;
    private final RabbitTemplate rabbitTemplate;

    // For SSE clients
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public OrderController(OrderRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
    order.setStatus("CREATED");
    order.setCreatedAt(java.time.LocalDateTime.now()); // set timestamp correctly
    Order savedOrder = repository.save(order);

        // publish event to RabbitMQ
        rabbitTemplate.convertAndSend("order.exchange", "order.created", savedOrder);

        // send to SSE clients immediately
        sendSseUpdate(savedOrder);

        return savedOrder;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    @GetMapping("/ping")
    public String ping() {
        return "Order Service is alive!";
    }

    // ✅ Update order status (called by Payment Service)
    @PatchMapping("/{id}")
    public Order updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        Order updatedOrder = repository.save(order);

        // push status change to SSE clients
        sendSseUpdate(updatedOrder);

        return updatedOrder;
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        repository.deleteById(id);
    }



    // ✅ Server-Sent Events endpoint
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrders() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // helper to send updates to all SSE clients
    private void sendSseUpdate(Order order) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order-update")
                        .data(order));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
}

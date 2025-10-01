package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.event.OrderCreatedEvent;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Order> getOrders() {
        logger.info("Fetching all orders");
        return repository.findAll();
    }

    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        Order savedOrder = repository.save(order);
        logger.info("Order created: {}", savedOrder.getId());

        double totalAmount = savedOrder.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getCustomerId())
                .amount(totalAmount)
                .build();

        kafkaTemplate.send("order.created", event);
        logger.info("Published order.created event for orderId: {}", savedOrder.getId());

        return savedOrder;
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = repository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            repository.save(order);
            logger.info("Order {} status updated to {}", orderId, status);
        } else {
            logger.warn("Order {} not found for status update", orderId);
        }
    }
}
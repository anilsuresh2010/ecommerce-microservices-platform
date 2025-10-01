package com.ecommerce.order_service.kafka;

import com.ecommerce.order_service.event.OrderCreatedEvent;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-processed", groupId = "order-group")
    public void handlePaymentProcessed(OrderCreatedEvent event) {
        log.info("Received payment-processed event for orderId: {}", event.getOrderId());
        orderService.updateOrderStatus(event.getOrderId(), "COMPLETED");
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    public void handlePaymentFailed(OrderCreatedEvent event) {
        log.info("Received payment-failed event for orderId: {}", event.getOrderId());
        orderService.updateOrderStatus(event.getOrderId(), "FAILED");
    }

    @KafkaListener(topics = "inventory-failed", groupId = "order-group")
    public void handleInventoryFailed(OrderCreatedEvent event) {
        log.info("Received inventory-failed event for orderId: {}", event.getOrderId());
        orderService.updateOrderStatus(event.getOrderId(), "FAILED");
    }
}

package com.ecommerce.notification_service.listner;

import com.ecommerce.notification_service.dto.NotificationRequest;
import com.ecommerce.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-completed", groupId = "notification-group")
    public void handleOrderCompleted(NotificationRequest     request) {
        log.info("Received order-completed event for orderId: {}", request.getOrderId());
        request.setMessage("Your order has been successfully completed!");
        notificationService.sendNotification(request);
    }

    @KafkaListener(topics = "order-failed", groupId = "notification-group")
    public void handleOrderFailed(NotificationRequest request) {
        log.info("Received order-failed event for orderId: {}", request.getOrderId());
        request.setMessage("Your order could not be processed. Please try again.");
        notificationService.sendNotification(request);
    }
}

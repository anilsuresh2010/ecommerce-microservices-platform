package com.ecommerce.notification_service.service;


import com.ecommerce.notification_service.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void sendNotification(NotificationRequest request) {
        // Simulate sending notification (email/SMS/log)
        log.info("🔔 Notification for user {}: {}", request.getUsername(), request.getMessage());
    }
}

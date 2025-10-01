package com.ecommerce.notification_service.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String orderId;
    private String username;
    private String message;
}

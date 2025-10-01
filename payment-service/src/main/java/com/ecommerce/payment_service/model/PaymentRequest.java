package com.ecommerce.payment_service.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String orderId;
    private String username;
    private double amount;
}

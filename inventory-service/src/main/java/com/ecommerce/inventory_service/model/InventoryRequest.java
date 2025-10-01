package com.ecommerce.inventory_service.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long productId;
    private int quantity;
    private String orderId;
}

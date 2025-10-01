package com.ecommerce.order_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private LocalDateTime orderDate;
    private String status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;
}

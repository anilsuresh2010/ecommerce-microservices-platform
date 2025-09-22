package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> getOrders(){
        return repository.findAll();
    }

    public Order createOrder(Order order){
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Created");
        return repository.save(order);
    }
}

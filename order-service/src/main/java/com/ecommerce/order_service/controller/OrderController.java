package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private final OrderService service;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        logger.info("Recieved request to create order ...");
        return ResponseEntity.ok(service.createOrder(order));
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder(){
        logger.info("Received request to fetch all the orders...");
        return ResponseEntity.ok(service.getOrders());
    }
    @PutMapping("/{orderId}/status")
    public void updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        service.updateOrderStatus(orderId, status);
    }


}

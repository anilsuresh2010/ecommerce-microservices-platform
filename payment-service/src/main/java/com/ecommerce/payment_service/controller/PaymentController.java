package com.ecommerce.payment_service.controller;

import com.ecommerce.payment_service.model.PaymentRequest;
import com.ecommerce.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    @Autowired
    private final PaymentService service;

    @PostMapping("/process")
    public void processPayment(@RequestBody PaymentRequest request) {
        log.info("API called to process payment: {}", request);
        service.processPayment(request);
    }
}

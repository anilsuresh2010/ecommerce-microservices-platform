package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.kafka.PaymentProducer;
import com.ecommerce.payment_service.model.PaymentRequest;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentProducer producer;
    private final RestTemplate restTemplate;

    @Value("${custom.user-service-url:http://localhost:8080/users}")
    private String userServiceUrl;

    public void processPayment(PaymentRequest request) {
        log.info("Processing payment for orderId: {}, user: {}, amount: ₹{}", request.getOrderId(), request.getUsername(), request.getAmount());

        String url = userServiceUrl + "/api/users/" + request.getUsername() + "/deduct?amount=" + request.getAmount();
        Boolean success = restTemplate.postForObject(url, null, Boolean.class);

        if (Boolean.TRUE.equals(success)) {
            log.info("✅ Payment successful for orderId: {}", request.getOrderId());
            producer.sendPaymentProcessedEvent(request);
        } else {
            log.warn("❌ Payment failed for orderId: {}", request.getOrderId());
            producer.sendPaymentFailedEvent(request);
        }
    }
}

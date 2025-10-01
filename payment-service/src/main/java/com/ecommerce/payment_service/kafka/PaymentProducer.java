package com.ecommerce.payment_service.kafka;

import com.ecommerce.payment_service.model.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;

    public void sendPaymentProcessedEvent(PaymentRequest request) {
        log.info("Sending PaymentProcessedEvent for orderId: {}", request.getOrderId());
        kafkaTemplate.send("payment-processed", request);
    }

    public void sendPaymentFailedEvent(PaymentRequest request) {
        log.info("Sending PaymentFailedEvent for orderId: {}", request.getOrderId());
        kafkaTemplate.send("payment-failed", request);
    }
}

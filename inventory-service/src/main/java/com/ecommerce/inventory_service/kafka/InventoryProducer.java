package com.ecommerce.inventory_service.kafka;


import com.ecommerce.inventory_service.model.InventoryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendInventoryReservedEvent(InventoryRequest request) {
        log.info("sending inventory reserve event...");
        send("inventory.reserved", request);
    }

    public void sendInventoryFailedEvent(InventoryRequest request) {
        log.info("send inventory failed evernt......");
        send("inventory.failed", request);
    }

    private void send(String topic, InventoryRequest request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(topic, message);
            log.info("📤 Sent event to {}: {}", topic, message);
        } catch (JsonProcessingException e) {
            log.error("❌ Failed to send Kafka event", e);
        }
    }
}


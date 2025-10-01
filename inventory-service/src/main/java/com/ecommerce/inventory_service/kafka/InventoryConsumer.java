package com.ecommerce.inventory_service.kafka;


import com.ecommerce.inventory_service.model.InventoryRequest;
import com.ecommerce.inventory_service.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumer {

    private final InventoryService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.created", groupId = "inventory-group")
    public void listenOrderCreated(String message) {
        try {
            log.info("Listen order create event inventory...");
            InventoryRequest request = objectMapper.readValue(message, InventoryRequest.class);
            log.info("📦 Received order.created event: {}", request);
            service.reserveStock(request);
        } catch (Exception e) {
            log.error("❌ Failed to process inventory event", e);
        }
    }
}

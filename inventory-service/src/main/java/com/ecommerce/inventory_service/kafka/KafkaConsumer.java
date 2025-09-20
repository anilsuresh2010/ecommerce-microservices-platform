package com.ecommerce.inventory_service.kafka;

import com.ecommerce.inventory_service.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

        private final InventoryService inventoryService;
        private final ObjectMapper objectMapper;

        public KafkaConsumer(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
            this.objectMapper = new ObjectMapper();
        }

        @KafkaListener(topics = "product-events", groupId = "inventory-group")
        public void consume(String message) throws JsonProcessingException {
            ProductEvent event = objectMapper.readValue(message, ProductEvent.class);
            inventoryService.createInventory(event.getId(), 100); // default quantity
        }
    }


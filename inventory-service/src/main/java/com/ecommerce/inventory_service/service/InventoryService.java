package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.kafka.InventoryProducer;
import com.ecommerce.inventory_service.model.InventoryItem;
import com.ecommerce.inventory_service.model.InventoryRequest;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    @Autowired
    private final InventoryRepository repository;
    @Autowired
    private final InventoryProducer producer;

    public void reserveStock(InventoryRequest request) {
        log.info("Received inventory reservation request for productId: {}", request.getProductId());

        InventoryItem item = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (item.getQuantity() >= request.getQuantity()) {
            item.setQuantity(item.getQuantity() - request.getQuantity());
            repository.save(item);
            log.info("Stock reserved for productId: {}, remaining quantity: {}", item.getProductId(), item.getQuantity());
            producer.sendInventoryReservedEvent(request);
        } else {
            log.warn("Insufficient stock for productId: {}", request.getProductId());
            producer.sendInventoryFailedEvent(request);
        }
    }

    public void rollbackStock(InventoryRequest request) {
        InventoryItem item = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        item.setQuantity(item.getQuantity() + request.getQuantity());
        repository.save(item);
        log.info("Rolled back stock for productId: {}, new quantity: {}", item.getProductId(), item.getQuantity());
    }

    public InventoryItem addInventory(InventoryItem item){
        return repository.save(item);
    }


}

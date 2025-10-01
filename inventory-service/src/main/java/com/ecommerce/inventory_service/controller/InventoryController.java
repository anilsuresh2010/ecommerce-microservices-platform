package com.ecommerce.inventory_service.controller;


import com.ecommerce.inventory_service.model.InventoryItem;
import com.ecommerce.inventory_service.model.InventoryRequest;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/reserve")
    public void reserveStock(@RequestBody InventoryRequest request) {
        log.info("API called to reserve stock: {}", request);
        service.reserveStock(request);
    }
    @PostMapping("/add")
    public InventoryItem addInventory(@RequestBody InventoryItem item) {
        return service.addInventory(item);
    }


    @PostMapping("/rollback")
    public void rollbackStock(@RequestBody InventoryRequest request) {
        log.info("API called to rollback stock: {}", request);
        service.rollbackStock(request);
    }
}


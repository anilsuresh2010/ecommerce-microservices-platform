package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.Inventary;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {

        this.repository = repository;
    }

    public Inventary createInventory(Long productId, Integer quantity){
        Inventary inventary = new Inventary();
        inventary.setProductId(productId);
        inventary.setQuantity(quantity);
        return repository.save(inventary);
    }

    public Inventary getInventoryById(Long productId){
        return repository.findByProductId(productId).orElseThrow(() -> new RuntimeException("Inventary not found"));
    }

    public Inventary updateQuantity(Long productId, int newQuantity){
               Inventary inventary = getInventoryById(productId);
               inventary.setQuantity(newQuantity);
        return repository.save(inventary);
    }
}

package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.model.Inventary;
import com.ecommerce.inventory_service.service.InventoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class InventaryController {

    private  final InventoryService service;


    public InventaryController(InventoryService service) {
        this.service = service;
    }
    @PutMapping("/{productId}/quantity")
    public Inventary updateQuantity(@PathVariable Long productId, @RequestParam int newQuantity){
        return service.updateQuantity(productId,newQuantity);
    }
}

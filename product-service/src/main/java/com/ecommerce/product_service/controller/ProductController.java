package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.model.ProductRequest;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @PostMapping("/create")
    public Product createProduct(@RequestBody ProductRequest request) {
        log.info("API called to create product: {}", request.getName());
        return service.createProduct(request);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{name}")
    public Product getProductByName(@PathVariable String name) {
        return service.getProductByName(name);
    }

    @PutMapping("/{id}/availability")
    public void updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        service.updateAvailability(id, available);
    }
}


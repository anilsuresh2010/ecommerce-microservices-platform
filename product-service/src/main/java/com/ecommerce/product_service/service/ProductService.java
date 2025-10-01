package com.ecommerce.product_service.service;


import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.model.ProductRequest;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(true);
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return repository.findAll();
    }

    public Product getProductByName(String name) {
        log.info("Fetching product by name: {}", name);
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void updateAvailability(Long productId, boolean available) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setAvailable(available);
        repository.save(product);
        log.info("Updated availability for product {}: {}", productId, available);
    }
}

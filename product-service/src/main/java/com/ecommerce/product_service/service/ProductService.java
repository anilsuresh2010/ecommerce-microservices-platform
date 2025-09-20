package com.ecommerce.product_service.service;


import com.ecommerce.product_service.kafka.KafkaProducer;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService{

    private final ProductRepository repository;
    private final KafkaProducer kafkaProducer;


    public ProductService(ProductRepository repository, KafkaProducer kafkaProducer) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
    }

    public Product createProduct(Product product) throws JsonProcessingException {
        Product saved= repository.save(product);
        kafkaProducer.sendProductCreatedEvent(saved);
        return saved;
    }

    public Product getProductById(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new RuntimeException("product not available.."));
    }

    public List<Product> getAllProduct(){
        return repository.findAll();
    }

    public void deleteProduct(Long id){
        repository.deleteById(id);
    }

    public Product updateProduct(Product newProduct, Long id){
        Product existingProduct = getProductById(id);
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        return repository.save(existingProduct);
    }

}

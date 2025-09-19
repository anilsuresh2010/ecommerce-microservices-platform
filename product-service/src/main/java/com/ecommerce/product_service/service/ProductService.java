package com.ecommerce.product_service.service;


import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService{

    private final ProductRepository repository;


    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product){
        return repository.save(product);
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
        existingProduct.setQuantity(newProduct.getQuantity());
        return repository.save(existingProduct);
    }

}

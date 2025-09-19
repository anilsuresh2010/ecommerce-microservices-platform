package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Product getByid(@PathVariable Long id){
        return service.getProductById(id);
    }

    @PostMapping
    public Product saveProduct(Product product){
        return  service.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProduct(){
        return service.getAllProduct();
    }

    @DeleteMapping("/{id}")
    public void removeProduct(@PathVariable Long id){
        service.deleteProduct(id);
    }

    public Product updateProduct(@RequestBody Product product, @PathVariable Long id){
        return service.updateProduct(product,id);
    }
}

package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.entity.Product;
import com.pizzamania.pizza_mania_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/products") // ✅ corrected annotation
public class ProductController {

    @Autowired
    private ProductService productService;

    // ✅ Add new product
    @PostMapping
    public String addProduct(@RequestBody Product product) throws ExecutionException, InterruptedException {
        return productService.addProduct(product);
    }

    // ✅ Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") String productId) throws ExecutionException, InterruptedException {
        return productService.getProductById(productId);
    }

    // ✅ Get all products
    @GetMapping
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        return productService.getAllProducts();
    }

    // ✅ Update product
    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") String productId, @RequestBody Product product) throws ExecutionException, InterruptedException {
        product.setProductId(productId); // Ensure ID matches path variable
        return productService.updateProduct(product);
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") String productId) throws ExecutionException, InterruptedException {
        return productService.deleteProduct(productId);
    }
}

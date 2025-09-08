package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


}

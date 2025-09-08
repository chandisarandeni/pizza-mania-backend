package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private String productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private boolean isAvailable;
}

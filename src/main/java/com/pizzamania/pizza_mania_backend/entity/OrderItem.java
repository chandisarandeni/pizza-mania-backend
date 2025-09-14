// File: src/main/java/com/pizzamania/pizza_mania_backend/entity/OrderItem.java
package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {
    private String productId;
    private String productName;
    private String productSize;
    private int quantity;
    private double price;
}

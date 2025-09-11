package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {

    private String orderItemId;
    private String orderId;
    private String productId;
    private String orderDescription;
    private float unitPrice;
    private int quantity;
    private float subtotal;
}

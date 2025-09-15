// File: src/main/java/com/pizzamania/pizza_mania_backend/entity/Order.java
package com.pizzamania.pizza_mania_backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {

    private String orderId;
    private String customerId;
    private String email;
    private String customerName;
    private String customerPhone;
    private String branchId;
    private float orderAmount;
    private String orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date orderDate;

    private List<OrderItem> items;
    private Payment payment;
}

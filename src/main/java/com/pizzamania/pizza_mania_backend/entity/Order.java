package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private String orderId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String branchId;
    private Date orderDate;
    private float orderAmount;
    private String orderStatus;
}

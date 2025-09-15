package com.pizzamania.pizza_mania_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private String orderId;
    private String customerId;
    private String email;
    private String customerName;
    private String customerPhone;
    private String branchId;
    private Date orderDate;
    private float orderAmount;
    private String orderStatus;
}

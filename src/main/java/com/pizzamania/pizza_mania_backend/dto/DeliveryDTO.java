package com.pizzamania.pizza_mania_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliveryDTO {

    private String deliveryId;
    private String orderId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String deliveryStatus;
    private Time outForDeliveryTime;
}

package com.pizzamania.pizza_mania_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String customerId; // Firebase document ID
    private String name;
    private String address;
    private String phone;
    private String email;
    private int otpCode;
    private long otpGeneratedTime;
}

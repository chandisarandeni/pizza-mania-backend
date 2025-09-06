package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {

    private String customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private int otpCode;
    private Date otpGeneratedTime;
}

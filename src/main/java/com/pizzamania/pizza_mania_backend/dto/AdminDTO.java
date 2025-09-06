package com.pizzamania.pizza_mania_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminDTO {
    private String adminId;
    private String name;
    private String username;
    private String password;
}

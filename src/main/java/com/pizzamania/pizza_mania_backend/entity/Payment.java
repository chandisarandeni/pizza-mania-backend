// File: src/main/java/com/pizzamania/pizza_mania_backend/entity/Payment.java
package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {
    private String method;        // e.g., "card", "cash"
    private String card_last4;    // last 4 digits if card
    private boolean authorized;   // true/false
}

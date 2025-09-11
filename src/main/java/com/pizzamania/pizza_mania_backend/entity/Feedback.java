package com.pizzamania.pizza_mania_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Feedback {

    private String feedbackId;
    private String orderId;
    private String customerId;
    private String customerName;
    private int rating;
    private String comment;
    private Date date;
}

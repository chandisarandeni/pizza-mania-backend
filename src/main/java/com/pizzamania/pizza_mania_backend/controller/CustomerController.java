package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


}

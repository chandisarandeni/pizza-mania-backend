package com.pizzamania.pizza_mania_backend.service;

import com.pizzamania.pizza_mania_backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

}

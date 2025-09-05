package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;


}

package com.pizzamania.pizza_mania_backend.service;

import com.pizzamania.pizza_mania_backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;


}

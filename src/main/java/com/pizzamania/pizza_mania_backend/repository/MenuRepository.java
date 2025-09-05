package com.pizzamania.pizza_mania_backend.repository;

import com.pizzamania.pizza_mania_backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}

package com.pizzamania.pizza_mania_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "menu")
public class Menu {

    @Id
    @Column(name = "menu_id")
    int menuId;

    @Column(name = "item_name")
    String itemName;

    @Column(name = "item_type")
    String itemType;

    @Column(name = "price")
    double price;
}

package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.dto.OrderItemDTO;
import com.pizzamania.pizza_mania_backend.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/orderitems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // Create new order item
    @PostMapping
    public String createOrderItem(@RequestBody OrderItemDTO orderItem) throws ExecutionException, InterruptedException {
        return orderItemService.addOrderItem(orderItem);
    }

    // Get all order items
    @GetMapping
    public List<OrderItemDTO> getAllOrderItems() throws ExecutionException, InterruptedException {
        return orderItemService.getAllOrderItems();
    }

    // Get order item by ID
    @GetMapping("/{id}")
    public OrderItemDTO getOrderItemById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return orderItemService.getOrderItemById(id);
    }

    // Update order item
    @PutMapping("/{id}")
    public String updateOrderItem(@PathVariable String id, @RequestBody OrderItemDTO orderItem) throws ExecutionException, InterruptedException {
        orderItem.setOrderItemId(id);
        return orderItemService.updateOrderItem(orderItem);
    }

    // Delete order item
    @DeleteMapping("/{id}")
    public String deleteOrderItem(@PathVariable String id) throws ExecutionException, InterruptedException {
        return orderItemService.deleteOrderItem(id);
    }

    // Get order items by orderId
    @GetMapping("/order/{orderId}")
    public List<OrderItemDTO> getOrderItemsByOrderId(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }
}

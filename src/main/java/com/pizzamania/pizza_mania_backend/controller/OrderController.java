package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.entity.Order;
import com.pizzamania.pizza_mania_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public String createOrder(@RequestBody Order order) throws ExecutionException, InterruptedException {
        return orderService.addOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() throws ExecutionException, InterruptedException {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public String updateOrder(@PathVariable String id, @RequestBody Order order) throws ExecutionException, InterruptedException {
        order.setOrderId(id);
        return orderService.updateOrder(order);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable String id) throws ExecutionException, InterruptedException {
        return orderService.deleteOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable String customerId) throws ExecutionException, InterruptedException {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @GetMapping("/branch/{branchId}")
    public List<Order> getOrdersByBranchId(@PathVariable String branchId) throws ExecutionException, InterruptedException {
        return orderService.getOrdersByBranchId(branchId);
    }

    @GetMapping("/email")
    public List<Order> getOrdersByCustomerEmail(@RequestParam String email)
            throws ExecutionException, InterruptedException {
        return orderService.getOrdersByCustomerEmail(email);
    }

}

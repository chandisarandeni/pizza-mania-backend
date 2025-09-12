package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.entity.Customer;
import com.pizzamania.pizza_mania_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        try {
            String id = customerService.saveCustomer(customer);
            return ResponseEntity.ok("Customer created with ID: " + id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating customer: " + e.getMessage());
        }
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Update customer
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        try {
            customer.setCustomerId(id);
            customerService.updateCustomer(customer);
            return ResponseEntity.ok("Customer updated with ID: " + id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating customer: " + e.getMessage());
        }
    }

    // Delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted with ID: " + id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting customer: " + e.getMessage());
        }
    }


    // --------------------- advanced features --------------------
    // Search customers by email
    @GetMapping("/get-by-email")
    public ResponseEntity<List<Customer>> searchCustomersByEmail(@RequestParam String email) {
        try {
            List<Customer> customers = customerService.searchCustomersByEmail(email);
            return ResponseEntity.ok(customers);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // --------------------- Send OTP --------------------
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        try {
            String result = customerService.sendOtp(email);
            return ResponseEntity.ok(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending OTP: " + e.getMessage());
        }
    }


    // ------------------ Verify OTP endpoint ------------------
    @GetMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        try {
            String result = customerService.verifyOtp(email, otp);
            if ("success".equals(result)) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error verifying OTP: " + e.getMessage());
        }
    }


    // reset password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String newPassword) {
        try {
            String result = customerService.resetPassword(email, newPassword);
            if (result.startsWith("Error")) {
                return ResponseEntity.badRequest().body(result);
            } else {
                return ResponseEntity.ok(result);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error resetting password: " + e.getMessage());
        }
    }


}

package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.entity.Feedback;
import com.pizzamania.pizza_mania_backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Create new feedback
    @PostMapping
    public String createFeedback(@RequestBody Feedback feedback) throws ExecutionException, InterruptedException {
        return feedbackService.addFeedback(feedback);
    }

    // Get all feedbacks
    @GetMapping
    public List<Feedback> getAllFeedbacks() throws ExecutionException, InterruptedException {
        return feedbackService.getAllFeedbacks();
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return feedbackService.getFeedbackById(id);
    }

    // Update feedback
    @PutMapping("/{id}")
    public String updateFeedback(@PathVariable String id, @RequestBody Feedback feedback) throws ExecutionException, InterruptedException {
        feedback.setFeedbackId(id);
        return feedbackService.updateFeedback(feedback);
    }

    // Delete feedback
    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable String id) throws ExecutionException, InterruptedException {
        return feedbackService.deleteFeedback(id);
    }

    // Get feedbacks by customerId
    @GetMapping("/customer/{customerId}")
    public List<Feedback> getFeedbacksByCustomerId(@PathVariable String customerId) throws ExecutionException, InterruptedException {
        return feedbackService.getFeedbacksByCustomerId(customerId);
    }

    // Get feedbacks by orderId
    @GetMapping("/order/{orderId}")
    public List<Feedback> getFeedbacksByOrderId(@PathVariable String orderId) throws ExecutionException, InterruptedException {
        return feedbackService.getFeedbacksByOrderId(orderId);
    }
}

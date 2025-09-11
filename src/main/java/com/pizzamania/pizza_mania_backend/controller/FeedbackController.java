package com.pizzamania.pizza_mania_backend.controller;

import com.pizzamania.pizza_mania_backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
}

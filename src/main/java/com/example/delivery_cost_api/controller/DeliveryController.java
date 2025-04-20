package com.example.delivery_cost_api.controller;

import com.example.delivery_cost_api.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/calculate-cost")
    public ResponseEntity<Map<String, Object>> calculateDeliveryCost(@RequestBody Map<String, Integer> order) {
        Map<String, Object> response = new HashMap<>();

        if (order == null || order.isEmpty()) {
            response.put("error", "Order must not be empty");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            int cost = deliveryService.calculateMinimumCost(order);
            response.put("minimumCost", cost);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Something went wrong: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

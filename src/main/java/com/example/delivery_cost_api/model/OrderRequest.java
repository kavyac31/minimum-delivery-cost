package com.example.delivery_cost_api.model;

import java.util.Map;

public class OrderRequest {
    private Map<String, Integer> order;

    public Map<String, Integer> getOrder() {
        return order;
    }

    public void setOrder(Map<String, Integer> order) {
        this.order = order;
    }
}


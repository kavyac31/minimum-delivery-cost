package com.example.delivery_cost_api.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeliveryService {

    private final Map<String, Map<String, Double>> centerProductWeights = Map.of(
        "C1", Map.of("A", 3.0, "B", 2.0, "C", 8.0),
        "C2", Map.of("D", 12.0, "E", 25.0, "F", 15.0),
        "C3", Map.of("G", 0.5, "H", 1.0, "I", 2.0)
    );

    private final Map<String, Map<String, Double>> distances = Map.of(
        "C1", Map.of("L1", 3.0, "C2", 4.0, "C3", 5.0),
        "C2", Map.of("L1", 2.5, "C1", 4.0, "C3", 3.0),
        "C3", Map.of("L1", 2.0, "C1", 5.0, "C2", 3.0),
        "L1", Map.of("C1", 3.0, "C2", 2.5, "C3", 2.0)
    );

    public int calculateMinimumCost(Map<String, Integer> order) {
        Set<String> centersInvolved = getInvolvedCenters(order);
        List<List<String>> permutations = generatePermutations(new ArrayList<>(centersInvolved));
        int minCost = Integer.MAX_VALUE;

        for (List<String> route : permutations) {
            int cost = simulateDelivery(route, order);
            minCost = Math.min(minCost, cost);
        }

        return minCost;
    }

    private int simulateDelivery(List<String> route, Map<String, Integer> order) {
        double totalCost = 0.0;
    
        for (int i = 0; i < route.size(); i++) {
            String center = route.get(i);
    
            // Calculate total weight to be picked from this center
            double centerWeight = 0.0;
            for (Map.Entry<String, Integer> entry : order.entrySet()) {
                String product = entry.getKey();
                int quantity = entry.getValue();
    
                if (centerProductWeights.containsKey(center) &&
                    centerProductWeights.get(center).containsKey(product)) {
                    double weight = centerProductWeights.get(center).get(product);
                    centerWeight += weight * quantity;
                }
            }
    
            // If this is not the first center, go from L1 to center (empty-handed)
            if (i != 0) {
                double travelToCenter = distances.get("L1").get(center);
                totalCost += getCostForDistance(travelToCenter, 0); // empty weight
            }
    
            // Now deliver from center to L1 with full weight
            double deliveryDistance = distances.get(center).get("L1");
            totalCost += getCostForDistance(deliveryDistance, centerWeight);
        }
    
        return (int) Math.round(totalCost);
    }
    

    private double getCostForDistance(double distance, double weight) {
        if (weight <= 5) return distance * 10;
        double extra = Math.ceil((weight - 5) / 5.0);
        return distance * (10 + extra * 8);
    }

    private Set<String> getInvolvedCenters(Map<String, Integer> order) {
        Set<String> centers = new HashSet<>();
        for (String product : order.keySet()) {
            for (String center : centerProductWeights.keySet()) {
                if (centerProductWeights.get(center).containsKey(product)) {
                    centers.add(center);
                }
            }
        }
        return centers;
    }

    private List<List<String>> generatePermutations(List<String> list) {
        List<List<String>> results = new ArrayList<>();
        permuteHelper(list, 0, results);
        return results;
    }

    private void permuteHelper(List<String> list, int index, List<List<String>> results) {
        if (index == list.size()) {
            results.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < list.size(); i++) {
            Collections.swap(list, i, index);
            permuteHelper(list, index + 1, results);
            Collections.swap(list, i, index);
        }
    }
}

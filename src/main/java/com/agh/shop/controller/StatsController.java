package com.agh.shop.controller;

import com.agh.shop.repository.ProductRepository;
import com.agh.shop.repository.OrderRepository;
import com.agh.shop.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatsController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Liczba produktów
        stats.put("totalProducts", productRepository.count());

        // Liczba wszystkich zamówień
        long totalOrders = orderRepository.count();
        stats.put("totalOrders", totalOrders);

        // Liczba zamówień według statusu
        long pendingOrders = orderRepository.findByStatus(OrderStatus.pending, null).getTotalElements();
        long processingOrders = orderRepository.findByStatus(OrderStatus.processing, null).getTotalElements();
        long shippedOrders = orderRepository.findByStatus(OrderStatus.shipped, null).getTotalElements();
        long deliveredOrders = orderRepository.findByStatus(OrderStatus.delivered, null).getTotalElements();

        stats.put("pendingOrders", pendingOrders);
        stats.put("processingOrders", processingOrders);
        stats.put("shippedOrders", shippedOrders);
        stats.put("deliveredOrders", deliveredOrders);

        return stats;
    }

    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats() {
        return getStats(); // Używamy tej samej logiki
    }
}
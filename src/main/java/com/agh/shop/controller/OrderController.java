package com.agh.shop.controller;

import com.agh.shop.model.OrderDTO;
import com.agh.shop.model.OrderResponse;
import com.agh.shop.model.OrderStatus;
import com.agh.shop.model.ShipOrderRequest;
import com.agh.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        // Walidacja parametrów
        if (limit < 1 || limit > 100) {
            limit = 50;
        }
        if (offset < 0) {
            offset = 0;
        }

        OrderResponse response = orderService.getAllOrders(status, limit, offset);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderDTO> shipOrder(
            @PathVariable Long id,
            @RequestBody ShipOrderRequest request) {
        OrderDTO order = orderService.shipOrder(id, request);
        return ResponseEntity.ok(order);
    }
}
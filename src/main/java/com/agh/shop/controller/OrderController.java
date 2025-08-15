package com.agh.shop.controller;

import com.agh.shop.model.OrderDTO;
import com.agh.shop.model.OrderRequest;
import com.agh.shop.model.OrderResponse;
import com.agh.shop.model.OrderStatus;
import com.agh.shop.model.ShipOrderRequest;
import com.agh.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 100;
    private static final int DEFAULT_OFFSET = 0;

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        int validatedLimit = validateLimit(limit);
        int validatedOffset = validateOffset(offset);
        OrderStatus orderStatus = parseOrderStatus(status);

        OrderResponse response = orderService.getAllOrders(orderStatus, validatedLimit, validatedOffset);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderDTO order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderDTO> shipOrder(
            @PathVariable Long id,
            @RequestBody ShipOrderRequest request) {
        OrderDTO order = orderService.shipOrder(id, request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderDTO> deliverOrder(@PathVariable Long id) {
        OrderDTO order = orderService.deliverOrder(id);
        return ResponseEntity.ok(order);
    }

    private int validateLimit(int limit) {
        if (limit < 1 || limit > MAX_LIMIT) {
            return DEFAULT_LIMIT;
        }
        return limit;
    }

    private int validateOffset(int offset) {
        return Math.max(offset, DEFAULT_OFFSET);
    }

    private OrderStatus parseOrderStatus(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }

        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
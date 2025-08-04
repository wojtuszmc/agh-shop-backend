package com.agh.shop.service;

import com.agh.shop.model.OrderDTO;
import com.agh.shop.model.OrderItemDTO;
import com.agh.shop.model.OrderResponse;
import com.agh.shop.model.ShipOrderRequest;
import com.agh.shop.model.OrderRequest;
import com.agh.shop.jpa.Order;
import com.agh.shop.jpa.OrderItem;
import com.agh.shop.model.OrderStatus;
import com.agh.shop.exception.BadRequestException;
import com.agh.shop.exception.ResourceNotFoundException;
import com.agh.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderResponse getAllOrders(OrderStatus status, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Order> page;

        if (status != null) {
            page = orderRepository.findByStatus(status, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        OrderResponse response = new OrderResponse();
        response.setOrders(page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        response.setTotal((int) page.getTotalElements());
        response.setLimit(limit);
        response.setOffset(offset);

        return response;
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zamówienie o ID " + id + " nie zostało znalezione"));
        return convertToDTO(order);
    }

    public OrderDTO createOrder(OrderRequest request) {
        Order order = new Order();
        
        // Generate order number if not provided
        String orderNumber;
        if (request.getOrderNumber() == null || request.getOrderNumber().trim().isEmpty()) {
            orderNumber = "AGH-" + System.currentTimeMillis();
        } else {
            orderNumber = request.getOrderNumber();
        }
        
        // Check if order number already exists
        if (orderRepository.findByOrderNumber(orderNumber).isPresent()) {
            throw new BadRequestException("Order number " + orderNumber + " already exists");
        }
        
        order.setOrderNumber(orderNumber);
        
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        
        // Create order items and establish bidirectional relationship
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderItemDTO itemDTO : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(itemDTO.getProductId());
                item.setProductName(itemDTO.getProductName());
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setTotalPrice(itemDTO.getTotalPrice());
                order.getItems().add(item);
            }
        }
        
        // Save order with items in one transaction
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public OrderDTO shipOrder(Long id, ShipOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zamówienie o ID " + id + " nie zostało znalezione"));

        // Sprawdź czy zamówienie może być wysłane
        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new BadRequestException("Zamówienie może być wysłane tylko ze statusu PROCESSING. Aktualny status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setTrackingNumber(request.getTrackingNumber());
        order.setCarrier(request.getCarrier());
        order.setShippedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCarrier(order.getCarrier());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setShippedAt(order.getShippedAt());
        dto.setDeliveredAt(order.getDeliveredAt());

        // Konwertuj pozycje zamówienia - bezpieczna inicjalizacja
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(new ArrayList<>());
        }

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}
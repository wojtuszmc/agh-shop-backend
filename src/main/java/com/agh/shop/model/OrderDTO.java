package com.agh.shop.model;

import com.agh.shop.jpa.Address;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Address shippingAddress;
    private Address billingAddress;
    private List<OrderItemDTO> items;
    private Double totalAmount;
    private OrderStatus status;
    private String trackingNumber;
    private String carrier;
    private LocalDateTime createdAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}

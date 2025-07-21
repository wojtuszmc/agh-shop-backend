package com.agh.shop.model;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}


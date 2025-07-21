package com.agh.shop.model;

import lombok.Data;

@Data
public class ShipOrderRequest {
    private String trackingNumber;
    private String carrier;
}

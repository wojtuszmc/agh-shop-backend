package com.agh.shop.model;

import com.agh.shop.jpa.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String orderNumber;

    @NotBlank(message = "Customer name is required")
    @Size(min = 1, max = 255, message = "Customer name must be between 1 and 255 characters")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Customer email must be valid")
    private String customerEmail;

    @NotBlank(message = "Customer phone is required")
    @Size(min = 9, max = 15, message = "Customer phone must be between 9 and 15 characters")
    private String customerPhone;

    @NotNull(message = "Shipping address is required")
    @Valid
    private Address shippingAddress;

    @NotNull(message = "Billing address is required")
    @Valid
    private Address billingAddress;

    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemDTO> items;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private Double totalAmount;

    private String status = "pending";
}
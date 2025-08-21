package com.agh.shop.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SimpleOrderItemRequest {
    @NotNull(message = "ID produktu jest wymagane")
    private Long productId;

    @NotNull(message = "Ilość jest wymagana")
    @Min(value = 1, message = "Ilość musi być większa niż 0")
    @Max(value = 100, message = "Maksymalna ilość to 100")
    private Integer quantity;
}
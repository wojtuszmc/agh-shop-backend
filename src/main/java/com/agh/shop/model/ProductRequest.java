package com.agh.shop.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Nazwa produktu jest wymagana")
    @Size(min = 1, max = 255, message = "Nazwa musi mieć od 1 do 255 znaków")
    private String name;

    @Size(max = 1000, message = "Opis nie może przekraczać 1000 znaków")
    private String description;

    @NotBlank(message = "Kategoria jest wymagana")
    @Size(min = 1, max = 100, message = "Kategoria musi mieć od 1 do 100 znaków")
    private String category;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa niż 0.01")
    private Double price;

    @NotNull(message = "Ilość jest wymagana")
    @Min(value = 0, message = "Ilość nie może być ujemna")
    private Integer quantity;
}
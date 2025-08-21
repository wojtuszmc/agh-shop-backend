package com.agh.shop.model;

import com.agh.shop.jpa.Address;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    private String orderNumber;

    @NotBlank(message = "Imię i nazwisko jest wymagane")
    @Size(min = 1, max = 255, message = "Imię i nazwisko musi mieć od 1 do 255 znaków")
    private String customerName;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Email musi być prawidłowy")
    private String customerEmail;

    @NotBlank(message = "Telefon jest wymagany")
    @Size(min = 9, max = 15, message = "Telefon musi mieć od 9 do 15 znaków")
    private String customerPhone;

    @NotNull(message = "Adres wysyłki jest wymagany")
    @Valid
    private Address shippingAddress;

    @NotNull(message = "Adres rozliczeniowy jest wymagany")
    @Valid
    private Address billingAddress;

    @NotEmpty(message = "Zamówienie musi zawierać przynajmniej jeden produkt")
    @Valid
    private List<SimpleOrderItemRequest> items;
}
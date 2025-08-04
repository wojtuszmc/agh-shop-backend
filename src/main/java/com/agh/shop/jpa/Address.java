package com.agh.shop.jpa;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotBlank(message = "Street is required")
    private String street;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = "Postal code must be in format XX-XXX")
    private String postalCode;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    private String state;
}
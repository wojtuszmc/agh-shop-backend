package com.agh.shop.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String code;
    private Map<String, Object> details;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}

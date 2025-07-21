package com.agh.shop.model;

import lombok.Data;
import java.util.List;

@Data
public class OrderResponse {
    private List<OrderDTO> orders;
    private Integer total;
    private Integer limit;
    private Integer offset;
}

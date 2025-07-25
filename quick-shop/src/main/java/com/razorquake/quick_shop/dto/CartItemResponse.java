package com.razorquake.quick_shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long id;
    private Integer quantity;
    private BigDecimal price;

    // Additional fields can be added as needed
}

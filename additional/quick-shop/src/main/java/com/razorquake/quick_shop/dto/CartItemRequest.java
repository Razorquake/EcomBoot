package com.razorquake.quick_shop.dto;

import lombok.Data;


@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}

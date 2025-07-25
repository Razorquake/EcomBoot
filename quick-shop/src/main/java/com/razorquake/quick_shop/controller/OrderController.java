package com.razorquake.quick_shop.controller;

import com.razorquake.quick_shop.dto.OrderResponse;
import com.razorquake.quick_shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") Long userId
    ) {
        return orderService.createOrder(userId)
                .map(orderResponse -> ResponseEntity.status(HttpStatus.CREATED).body(orderResponse))
                .orElse(ResponseEntity.badRequest().build());
    }
}

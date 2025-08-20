package com.razorquake.quick_shop.controller;

import com.razorquake.quick_shop.dto.CartItemRequest;
import com.razorquake.quick_shop.dto.CartItemResponse;
import com.razorquake.quick_shop.model.CartItem;
import com.razorquake.quick_shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") Long userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        if (cartService.addToCart(userId, cartItemRequest))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity
                    .badRequest()
                    .body("Product Out of Stock or User Not Found or Product Not Found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("X-User-ID") Long userId,
            @PathVariable Long productId
    ) {
        if (cartService.removeFromCart(userId, productId)) {
            return ResponseEntity.ok("Item removed from cart successfully");
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Product Not Found in Cart or User Not Found");
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(
            @RequestHeader("X-User-ID") Long userId
    ) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cartItems);
    }

}

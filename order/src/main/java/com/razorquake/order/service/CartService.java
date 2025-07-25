package com.razorquake.order.service;


import com.razorquake.order.dto.CartItemRequest;
import com.razorquake.order.model.CartItem;
import com.razorquake.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
//        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
//        if (productOpt.isEmpty()) {
//            return false; // Product not found
//        }
//        Product product = productOpt.get();
//        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
//            return false; // Not enough stocks
//        }
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            return false; // User not found
//        }
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());

        if (existingCartItem != null) {
            // Update existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(BigDecimal.ZERO);
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = mapToCartItem(cartItemRequest, userId, cartItemRequest.getProductId());
            cartItemRepository.save(newCartItem);
        }
        return true; // Item added to cart successfully

    }

    private CartItem mapToCartItem(CartItemRequest cartItemRequest, String userId, String productId) {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(BigDecimal.ZERO);
        return cartItem;
    }

    public boolean removeFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true; // Item removed from cart successfully
        }

        return false; // Product not found in cart or User isn't found
    }

    public List<CartItem> getCartItems(String userId) {
        return cartItemRepository.findAllByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }

}

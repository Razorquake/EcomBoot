package com.razorquake.order.service;


import com.razorquake.order.client.ProductServiceClient;
import com.razorquake.order.client.UserServiceClient;
import com.razorquake.order.dto.CartItemRequest;
import com.razorquake.order.dto.ProductResponse;
import com.razorquake.order.dto.UserResponse;
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
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        ProductResponse product = productServiceClient.getProductById(cartItemRequest.getProductId());
        if (product == null) {
            return false; // Product not found
        }
        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
            return false; // Not enough stocks
        }
        UserResponse user = userServiceClient.getUserById(userId);
        if (user == null) {
            return false;
        }
//        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
//        Product product = productOpt.get();
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            return false; // User not found
//        }
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());

        if (existingCartItem != null) {
            // Update existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = mapToCartItem(cartItemRequest, userId, cartItemRequest.getProductId(), product);
            cartItemRepository.save(newCartItem);
        }
        return true; // Item added to cart successfully

    }

    private CartItem mapToCartItem(CartItemRequest cartItemRequest, String userId, String productId, ProductResponse product) {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
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

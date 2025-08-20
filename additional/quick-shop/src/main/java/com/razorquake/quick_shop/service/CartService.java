package com.razorquake.quick_shop.service;

import com.razorquake.quick_shop.dto.CartItemRequest;
import com.razorquake.quick_shop.dto.CartItemResponse;
import com.razorquake.quick_shop.model.CartItem;
import com.razorquake.quick_shop.model.Product;
import com.razorquake.quick_shop.model.User;
import com.razorquake.quick_shop.repository.CartItemRepository;
import com.razorquake.quick_shop.repository.ProductRepository;
import com.razorquake.quick_shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(Long userId, CartItemRequest cartItemRequest) {
        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
        if (productOpt.isEmpty()) {
            return false; // Product not found
        }
        Product product = productOpt.get();
        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
            return false; // Not enough stocks
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false; // User not found
        }
        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            // Update existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = mapToCartItem(cartItemRequest, user, product);
            cartItemRepository.save(newCartItem);
        }
        return true; // Item added to cart successfully

    }

    private CartItem mapToCartItem(CartItemRequest cartItemRequest, User user, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
        return cartItem;
    }

    public boolean removeFromCart(Long userId, Long productId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent() && userOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true; // Item removed from cart successfully
        }

        return false; // Product not found in cart or User isn't found
    }

    public List<CartItem> getCartItems(Long userId) {
        return userRepository.findById(userId)
                .map(cartItemRepository::findAllByUser)
                .orElseGet(List::of);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());
        return response;
    }

    public void clearCart(Long userId) {
        userRepository.findById(userId).ifPresent(
                cartItemRepository::deleteByUser
        );
    }

}

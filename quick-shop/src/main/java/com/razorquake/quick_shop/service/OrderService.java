package com.razorquake.quick_shop.service;

import com.razorquake.quick_shop.dto.OrderItemDTO;
import com.razorquake.quick_shop.dto.OrderResponse;
import com.razorquake.quick_shop.model.*;
import com.razorquake.quick_shop.repository.OrderRepository;
import com.razorquake.quick_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    public Optional<OrderResponse> createOrder(Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty(); // No items in the cart to create an order
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty(); // User not found
        }
        Order order = new Order();
        order.setUser(userOpt.get());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(
                cartItems.stream()
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);  // Clear the cart after order creation

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).toList(),
                order.getCreatedAt());
    }



}

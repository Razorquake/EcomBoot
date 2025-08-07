package com.razorquake.order.service;

import com.razorquake.order.dto.OrderCreatedEvent;
import com.razorquake.order.dto.OrderItemDTO;
import com.razorquake.order.dto.OrderResponse;
import com.razorquake.order.model.CartItem;
import com.razorquake.order.model.OrderStatus;
import com.razorquake.order.model.Order;
import com.razorquake.order.model.OrderItem;
import com.razorquake.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final RabbitTemplate rabbitTemplate;

    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty(); // No items in the cart to create an order
        }
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            return Optional.empty(); // User not found
//        }
        Order order = new Order();
        order.setUserId(userId);

        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(
                cartItems.stream()
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);  // Clear the cart after order creation

        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.tracking",
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getUserId(),
                        savedOrder.getStatus(),
                        savedOrder.getTotalAmount(),
                        mapToOrderItemDTOs(savedOrder.getItems()),
                        savedOrder.getCreatedAt()
                )
        );

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private List<OrderItemDTO> mapToOrderItemDTOs(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                mapToOrderItemDTOs(order.getItems()),
                order.getCreatedAt());
    }



}

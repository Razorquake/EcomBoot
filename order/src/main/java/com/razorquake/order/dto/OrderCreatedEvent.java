package com.razorquake.order.dto;

import com.razorquake.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> item;
    private LocalDateTime createdAt;

}

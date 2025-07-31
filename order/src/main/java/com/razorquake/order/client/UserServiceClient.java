package com.razorquake.order.client;

import com.razorquake.order.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {
    @GetExchange("/api/users/{id}")
    UserResponse getUserById(@PathVariable String id);
}

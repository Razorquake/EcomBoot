package com.razorquake.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private UserRole role;
    private AddressDTO address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

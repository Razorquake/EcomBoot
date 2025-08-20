package com.razorquake.user.dto;

import com.razorquake.user.model.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String keycloakId;
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

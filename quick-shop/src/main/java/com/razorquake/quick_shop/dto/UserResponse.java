package com.razorquake.quick_shop.dto;

import com.razorquake.quick_shop.model.UserRole;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private UserRole role;
    private AddressDTO addressDTO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.razorquake.quick_shop.dto;


import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private AddressDTO addressDTO;
}

package com.razorquake.user.dto;


import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private AddressDTO address;
}

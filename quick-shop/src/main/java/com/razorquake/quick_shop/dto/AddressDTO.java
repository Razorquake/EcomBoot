package com.razorquake.quick_shop.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

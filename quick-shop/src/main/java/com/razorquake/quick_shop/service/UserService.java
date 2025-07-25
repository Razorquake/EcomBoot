package com.razorquake.quick_shop.service;

import com.razorquake.quick_shop.dto.AddressDTO;
import com.razorquake.quick_shop.dto.UserRequest;
import com.razorquake.quick_shop.dto.UserResponse;
import com.razorquake.quick_shop.model.Address;
import com.razorquake.quick_shop.model.User;
import com.razorquake.quick_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(
                this::mapToUserResponse
        ).toList();
    }
    public void createUser(UserRequest userRequest) {
        userRepository.save(mapToUser(new User(), userRequest));
    }


    public Optional<UserResponse> getUser(Long id) {
        return userRepository.findById(id).map(
                this::mapToUserResponse
        );
    }

    public Optional<UserResponse> updateUser(Long id, UserRequest user) {
        return userRepository.findById(id)
                .map(existingUser -> mapToUserResponse(
                        userRepository.save(mapToUser(existingUser, user))
                ));
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        if (user.getAddress() != null) {
            response.setAddressDTO(mapToAddressDTO(user.getAddress()));
        }
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    private Address mapToAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    private User mapToUser(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        if (userRequest.getAddressDTO() != null) {
            user.setAddress(mapToAddress(userRequest.getAddressDTO()));
        }

        return user;
    }
}

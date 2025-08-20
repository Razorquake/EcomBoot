package com.razorquake.user.service;

import com.razorquake.user.dto.AddressDTO;
import com.razorquake.user.dto.UserRequest;
import com.razorquake.user.dto.UserResponse;
import com.razorquake.user.model.Address;
import com.razorquake.user.model.User;
import com.razorquake.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KeyCloakAdminService keyCloakAdminService;
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(
                this::mapToUserResponse
        ).toList();
    }
    public void createUser(UserRequest userRequest) {
        String keycloakUserId = keyCloakAdminService.createUser(
                keyCloakAdminService.getAccessToken(),
                userRequest
        );
        User user = mapToUser(new User(), userRequest);
        user.setKeycloakId(keycloakUserId);
        keyCloakAdminService.assignRealmRoleToUser(keycloakUserId, "USER");
        userRepository.save(user);
    }


    public Optional<UserResponse> getUser(String id) {
        return userRepository.findById(id).map(
                this::mapToUserResponse
        );
    }

    public Optional<UserResponse> updateUser(String id, UserRequest user) {
        return userRepository.findById(id)
                .map(existingUser -> mapToUserResponse(
                        userRepository.save(mapToUser(existingUser, user))
                ));
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        if (user.getAddress() != null) {
            response.setAddress(mapToAddressDTO(user.getAddress()));
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
        if (userRequest.getAddress() != null) {
            user.setAddress(mapToAddress(userRequest.getAddress()));
        }
        return user;
    }
}

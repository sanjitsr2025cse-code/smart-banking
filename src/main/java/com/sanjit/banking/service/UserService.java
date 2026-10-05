package com.sanjit.banking.service;

import com.sanjit.banking.dto.UserRequest;
import com.sanjit.banking.dto.UserResponse;
import com.sanjit.banking.dto.UserUpdateRequest;
import com.sanjit.banking.entity.User;

public interface UserService {

    UserResponse getUserById(Long id);

    User getUserByEmail(String email);

    UserResponse updateUser(
            Long id,
            UserUpdateRequest userUpdateRequest
    );

    void deleteUser(Long id);

    UserResponse registerUser(UserRequest userRequest);
}
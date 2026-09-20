package com.sanjit.banking.service;

import com.sanjit.banking.dto.UserRequest;
import com.sanjit.banking.entity.User;

public interface UserService {
    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateUser(User user);
    void deleteUser(Long id);
    User registerUser(UserRequest userRequest);

}

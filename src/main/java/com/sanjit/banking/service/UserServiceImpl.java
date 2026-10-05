package com.sanjit.banking.service;

import com.sanjit.banking.dto.UserRequest;
import com.sanjit.banking.dto.UserResponse;
import com.sanjit.banking.dto.UserUpdateRequest;
import com.sanjit.banking.entity.User;
import com.sanjit.banking.exception.ResourceNotFoundException;
import com.sanjit.banking.exception.ConflictException;
import com.sanjit.banking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getUserById(Long id) {

        Optional<User> existingUser =
                userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        User user = existingUser.get();

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UserUpdateRequest userUpdateRequest) {

        Optional<User> existingUser =
                userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        User user = existingUser.get();

        user.setName(userUpdateRequest.getName());
        user.setEmail(userUpdateRequest.getEmail());

        User updatedUser =
                userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt()
        );
    }

    @Override
    public User getUserByEmail(String email) {

        Optional<User> existingUser =
                userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        return existingUser.get();
    }

    @Override
    public void deleteUser(Long id) {

        Optional<User> existingUser =
                userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserResponse registerUser(
            UserRequest userRequest) {

        Optional<User> existingUser =
                userRepository.findByEmail(
                        userRequest.getEmail()
                );

        if (existingUser.isPresent()) {
            throw new ConflictException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        String hashedPassword =
                passwordEncoder.encode(
                        userRequest.getPassword()
                );

        user.setPassword(hashedPassword);
        user.setRole("USER");

        User savedUser =
                userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}
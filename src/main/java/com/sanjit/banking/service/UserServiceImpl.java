package com.sanjit.banking.service;

import com.sanjit.banking.dto.UserRequest;
import com.sanjit.banking.entity.User;
import com.sanjit.banking.exception.ResourceNotFoundException;
import com.sanjit.banking.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
     Optional<User> exisitingUser = userRepository.findById(id);
     if(exisitingUser.isEmpty()){
         throw new ResourceNotFoundException("User not found");
     }
     return exisitingUser.get();
    }

    @Override
    public User getUserByEmail(String email) {
       Optional<User> exisitingUser = userRepository.findByEmail(email);
       if(exisitingUser.isEmpty()){
           throw new ResourceNotFoundException("User not found");
       }
       return exisitingUser.get();
    }

    @Override
    public User updateUser(User user) {
        Optional<User> existingUser=userRepository.findById(user.getId());
        if(existingUser.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User usertoupdate=existingUser.get();
        usertoupdate.setEmail(user.getEmail());

        return userRepository.save(usertoupdate);

    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> existingUser=userRepository.findById(id);
        if(existingUser.isEmpty()){
            throw new ResourceNotFoundException("user not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User registerUser(UserRequest userRequest) {

        Optional<User> existingUser =
                userRepository.findByEmail(userRequest.getEmail());

        if (existingUser.isPresent()) {
            throw new ResourceNotFoundException("Email already registered");
        }

        return userRepository.save(User);
    }
}
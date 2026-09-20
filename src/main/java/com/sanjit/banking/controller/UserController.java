package com.sanjit.banking.controller;

import com.sanjit.banking.dto.UserRequest;
import com.sanjit.banking.dto.UserResponse;
import com.sanjit.banking.entity.User;
import com.sanjit.banking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
       return userService.getUserById(id);
    }
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.registerUser(UserRequest), HttpStatus.CREATED);
    }



}

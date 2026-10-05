package com.sanjit.banking.controller;

import com.sanjit.banking.dto.LoginRequest;
import com.sanjit.banking.dto.LoginResponse;
import com.sanjit.banking.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse response =
                authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }
}
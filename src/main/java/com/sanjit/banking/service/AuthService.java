package com.sanjit.banking.service;

import com.sanjit.banking.dto.LoginRequest;
import com.sanjit.banking.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

}

package com.sanjit.banking.security;

import com.sanjit.banking.entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    boolean validateToken(String token);
}
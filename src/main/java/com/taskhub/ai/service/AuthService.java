package com.taskhub.ai.service;

import com.taskhub.ai.dto.RegisterRequest;
import com.taskhub.ai.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(String username, String password);
    Boolean register(RegisterRequest registerRequest);
    TokenResponse refreshAccessToken(String refreshToken);
}
package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.LoginRequest;
import com.taskhub.ai.dto.RefreshRequest;
import com.taskhub.ai.dto.RegisterRequest;
import com.taskhub.ai.dto.TokenResponse;
import com.taskhub.ai.security.JwtTokenProvider;
import com.taskhub.ai.service.AuthService;
import com.taskhub.ai.service.TokenBlacklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public Result<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return Result.success(authService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return Result.success(authService.register(registerRequest));
    }

    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return Result.success(authService.refreshAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            long ttl = jwtTokenProvider.getTokenRemainingTime(token);
            tokenBlacklistService.blacklistToken(token, ttl);
        }
        return Result.success();
    }
}
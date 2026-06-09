package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.UserEntity;
import com.taskhub.ai.dto.RegisterRequest;
import com.taskhub.ai.dto.TokenResponse;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.security.JwtTokenProvider;
import com.taskhub.ai.service.AuthService;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    @Transactional
    public Boolean register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("用户名已存在");
        }
        UserEntity user = new UserEntity();
        user.setId(snowflakeIdGenerator.nextId());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        user.setIsAdmin(0);
        userRepository.save(user);
        return true;
    }

    @Override
    public TokenResponse login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String accessToken = jwtTokenProvider.createAccessToken(username, user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(username, user.getId());
        return new TokenResponse(accessToken, refreshToken, "Bearer", jwtTokenProvider.getAccessTokenExpiration() / 1000);
    }

    @Override
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("Refresh token 无效或已过期，请重新登录");
        }
        String username = jwtTokenProvider.getUsername(refreshToken);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(username, user.getId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(username, user.getId());
        return new TokenResponse(newAccessToken, newRefreshToken, "Bearer", jwtTokenProvider.getAccessTokenExpiration() / 1000);
    }
}
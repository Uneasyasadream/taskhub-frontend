package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.UserEntity;
import com.taskhub.ai.dto.UserUpdateDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.service.UserService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserVO getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO updateCurrentUser(UserUpdateDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        return convertToVO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserVO updateAvatar(String avatarUrl) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setAvatar(avatarUrl);
        return convertToVO(userRepository.save(user));
    }

    private UserVO convertToVO(UserEntity entity) {
        UserVO vo = new UserVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setUsername(entity.getUsername());
        vo.setNickname(entity.getNickname());
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        vo.setAvatar(entity.getAvatar());
        vo.setStatus(entity.getStatus());
        vo.setIsAdmin(entity.getIsAdmin());
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());
        return vo;
    }
}
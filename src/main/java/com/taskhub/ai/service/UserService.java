package com.taskhub.ai.service;

import com.taskhub.ai.dto.UserUpdateDTO;
import com.taskhub.ai.vo.UserVO;

public interface UserService {
    UserVO getCurrentUser();
    UserVO updateCurrentUser(UserUpdateDTO dto);
    void changePassword(String oldPassword, String newPassword);
    UserVO updateAvatar(String avatarUrl);
}
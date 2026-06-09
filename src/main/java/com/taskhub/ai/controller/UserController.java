package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.PasswordChangeDTO;
import com.taskhub.ai.dto.UserUpdateDTO;
import com.taskhub.ai.service.UserService;
import com.taskhub.ai.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public Result<UserVO> updateProfile(@Valid @RequestBody UserUpdateDTO dto) {
        return Result.success(userService.updateCurrentUser(dto));
    }

    @PutMapping("/me/password")
    public Result<Void> changePassword(@Valid @RequestBody PasswordChangeDTO dto) {
        userService.changePassword(dto.getOldPassword(), dto.getNewPassword());
        return Result.success();
    }

    @PutMapping("/me/avatar")
    public Result<UserVO> updateAvatar(@RequestParam String avatarUrl) {
        return Result.success(userService.updateAvatar(avatarUrl));
    }
}
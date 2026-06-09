package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = fileUploadService.uploadAvatar(file);
        return Result.success(avatarUrl);
    }
}
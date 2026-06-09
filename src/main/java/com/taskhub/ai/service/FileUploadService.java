package com.taskhub.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadAvatar(MultipartFile file);
}
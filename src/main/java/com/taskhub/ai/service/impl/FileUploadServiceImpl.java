package com.taskhub.ai.service.impl;

import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.service.FileUploadService;
import com.taskhub.ai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir:./uploads/avatars}")
    private String uploadDir;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String uploadAvatar(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;
        Long userId = SecurityUtils.getCurrentUserId();
        String userDir = uploadDir + "/" + userId;
        try {
            Path dirPath = Paths.get(userDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return baseUrl + "/api/v1/upload/avatars/" + userId + "/" + fileName;
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }
    }
}
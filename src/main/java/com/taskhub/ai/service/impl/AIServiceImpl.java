package com.taskhub.ai.service.impl;

import com.taskhub.ai.service.AIService;
import com.taskhub.ai.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final DeepSeekService deepSeekService;

    @Override
    public String generateContent(String prompt) {
        return deepSeekService.generateContent(prompt);
    }

    @Override
    public CompletableFuture<String> generateContentAsync(String prompt) {
        return deepSeekService.generateContentAsync(prompt);
    }
}
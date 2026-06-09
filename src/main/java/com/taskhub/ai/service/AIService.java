package com.taskhub.ai.service;

import java.util.concurrent.CompletableFuture;

public interface AIService {
    String generateContent(String prompt);
    CompletableFuture<String> generateContentAsync(String prompt);
}
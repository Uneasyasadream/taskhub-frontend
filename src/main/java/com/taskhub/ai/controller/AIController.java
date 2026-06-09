package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.AIChatRequest;
import com.taskhub.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/generate")
    public Result<String> generateContent(@RequestBody AIChatRequest request) {
        String result = aiService.generateContent(request.getPrompt());
        return Result.success(result);
    }
}
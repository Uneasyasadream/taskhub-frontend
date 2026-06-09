package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BugCreateDTO {
    @NotBlank(message = "缺陷标题不能为空")
    private String title;
    private String severity;
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
}
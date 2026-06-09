package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "任务标题不能为空")
    private String title;
    private String description;
    private Integer status;
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    private Long assigneeId;
}
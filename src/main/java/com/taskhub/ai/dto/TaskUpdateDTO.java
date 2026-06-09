package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskUpdateDTO {
    @NotBlank
    private String title;
    private String description;
    private Integer status;
    private Long assigneeId;
}
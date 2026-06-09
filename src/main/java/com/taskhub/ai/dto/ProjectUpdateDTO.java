package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectUpdateDTO {
    @NotBlank
    private String name;
    private String description;
}
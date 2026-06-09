package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BugUpdateDTO {
    @NotBlank
    private String title;
    private String severity;
}
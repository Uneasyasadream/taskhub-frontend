package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SprintCreateDTO {
    @NotBlank
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    private Long projectId;
}
package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SprintUpdateDTO {
    @NotBlank
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectMemberAddDTO {
    @NotNull
    private Long userId;
    @NotNull
    private String role; // ADMIN, MEMBER, VIEWER
}
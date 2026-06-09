package com.taskhub.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectMemberUpdateRoleDTO {
    @NotNull
    private String role;
}
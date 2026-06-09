package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectMemberVO {
    private String id;
    private String userId;
    private String username;
    private String nickname;
    private String projectId;
    private String role;
    private LocalDateTime createdTime;
}
package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectVO {
    private String id;
    private String name;
    private String description;
    private String ownerId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskVO {
    private String id;
    private String title;
    private String description;
    private Integer status;
    private String projectId;
    private String assigneeId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
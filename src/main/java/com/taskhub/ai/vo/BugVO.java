package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BugVO {
    private String id;
    private String title;
    private String severity;
    private String projectId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
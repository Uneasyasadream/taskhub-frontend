package com.taskhub.ai.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SprintVO {
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
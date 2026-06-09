package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "task")
@Getter
@Setter
@ToString(callSuper = true)
@SQLRestriction("is_deleted = 0")
public class TaskEntity extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "assignee_id")
    private Long assigneeId;
}
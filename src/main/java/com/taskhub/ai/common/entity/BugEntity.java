package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "bug")
@Getter
@Setter
@ToString(callSuper = true)
@SQLRestriction("is_deleted = 0")
public class BugEntity extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "severity")
    private String severity;

    @Column(name = "project_id", nullable = false)
    private Long projectId;
}
package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "project_member")
@Getter
@Setter
@ToString(callSuper = true)
@SQLRestriction("is_deleted = 0")
public class ProjectMemberEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "role", nullable = false, length = 20)
    private String role; // ADMIN, MEMBER, VIEWER
}
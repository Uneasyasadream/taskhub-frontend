package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString(callSuper = true)
@SQLRestriction("is_deleted = 0")
public class ProjectEntity extends BaseEntity {
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
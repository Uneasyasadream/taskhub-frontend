package com.taskhub.ai.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "sprint")
@Getter
@Setter
@ToString(callSuper = true)
@SQLRestriction("is_deleted = 0")
public class SprintEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "project_id", nullable = false)
    private Long projectId;
}
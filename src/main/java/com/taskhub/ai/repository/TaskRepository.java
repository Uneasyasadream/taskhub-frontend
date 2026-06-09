package com.taskhub.ai.repository;

import com.taskhub.ai.common.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findByProjectId(Long projectId, Pageable pageable);
    List<TaskEntity> findByProjectId(Long projectId);
}
package com.taskhub.ai.repository;

import com.taskhub.ai.common.entity.SprintEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {
    List<SprintEntity> findByProjectId(Long projectId);
    Page<SprintEntity> findByProjectId(Long projectId, Pageable pageable);
}
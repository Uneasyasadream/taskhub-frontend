package com.taskhub.ai.repository;

import com.taskhub.ai.common.entity.BugEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<BugEntity, Long> {
    List<BugEntity> findByProjectId(Long projectId);
    Page<BugEntity> findByProjectId(Long projectId, Pageable pageable);
}
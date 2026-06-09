package com.taskhub.ai.repository;

import com.taskhub.ai.common.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwnerId(Long ownerId);
    Page<ProjectEntity> findByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "SELECT * FROM project WHERE id = :id", nativeQuery = true)
    Optional<ProjectEntity> findByIdRaw(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE project SET is_deleted = 1 WHERE id = :id", nativeQuery = true)
    int softDeleteById(@Param("id") Long id);
}
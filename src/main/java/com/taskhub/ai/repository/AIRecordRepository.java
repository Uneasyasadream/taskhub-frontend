package com.taskhub.ai.repository;

import com.taskhub.ai.common.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AIRecordRepository extends JpaRepository<BaseEntity, Long> {

}
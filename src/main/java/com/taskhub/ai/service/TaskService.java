package com.taskhub.ai.service;

import com.taskhub.ai.dto.TaskCreateDTO;
import com.taskhub.ai.dto.TaskUpdateDTO;
import com.taskhub.ai.vo.TaskVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskVO createTask(TaskCreateDTO dto);
    TaskVO updateTask(Long id, TaskUpdateDTO dto);
    void deleteTask(Long id);
    TaskVO getTaskById(Long id);
    Page<TaskVO> listTasksByProject(Long projectId, Pageable pageable);
}
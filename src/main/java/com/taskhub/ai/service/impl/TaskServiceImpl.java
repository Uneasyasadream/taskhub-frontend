package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.TaskEntity;
import com.taskhub.ai.dto.TaskCreateDTO;
import com.taskhub.ai.dto.TaskUpdateDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.TaskRepository;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.service.TaskService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import com.taskhub.ai.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final ProjectMemberService projectMemberService;
    private final UserRepository userRepository;

    private boolean isAdmin() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userRepository.findById(userId)
                .map(u -> u.getIsAdmin() == 1)
                .orElse(false);
    }

    @Override
    @Transactional
    public TaskVO createTask(TaskCreateDTO dto) {
        if (!(isAdmin() || projectMemberService.isMember(dto.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        if (dto.getAssigneeId() != null && !projectMemberService.isMember(dto.getProjectId(), dto.getAssigneeId())) {
            throw new BusinessException("负责人不是项目成员");
        }
        TaskEntity entity = new TaskEntity();
        entity.setId(idGenerator.nextId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setProjectId(dto.getProjectId());
        entity.setAssigneeId(dto.getAssigneeId());
        return convertToVO(taskRepository.save(entity));
    }

    @Override
    @Transactional
    public TaskVO updateTask(Long id, TaskUpdateDTO dto) {
        TaskEntity entity = getTaskEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        if (dto.getAssigneeId() != null && !projectMemberService.isMember(entity.getProjectId(), dto.getAssigneeId())) {
            throw new BusinessException("负责人不是项目成员");
        }
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            Integer oldStatus = entity.getStatus();
            Integer newStatus = dto.getStatus();
            if (oldStatus == 0 && newStatus == 2) {
                throw new BusinessException("待办任务不能直接变为已完成");
            }
            if (oldStatus == 2 && newStatus != 2) {
                throw new BusinessException("已完成任务不可修改状态");
            }
            entity.setStatus(newStatus);
        }
        entity.setAssigneeId(dto.getAssigneeId());
        return convertToVO(taskRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        TaskEntity entity = getTaskEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        entity.setIsDeleted(1);
        taskRepository.save(entity);
    }

    @Override
    public TaskVO getTaskById(Long id) {
        TaskEntity entity = getTaskEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return convertToVO(entity);
    }

    @Override
    public Page<TaskVO> listTasksByProject(Long projectId, Pageable pageable) {
        if (!(isAdmin() || projectMemberService.isMember(projectId, SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return taskRepository.findByProjectId(projectId, pageable).map(this::convertToVO);
    }

    private TaskEntity getTaskEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("任务不存在"));
    }

    private TaskVO convertToVO(TaskEntity entity) {
        TaskVO vo = new TaskVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setTitle(entity.getTitle());
        vo.setDescription(entity.getDescription());
        vo.setStatus(entity.getStatus());
        vo.setProjectId(String.valueOf(entity.getProjectId()));
        vo.setAssigneeId(entity.getAssigneeId() != null ? String.valueOf(entity.getAssigneeId()) : null);
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());
        return vo;
    }
}
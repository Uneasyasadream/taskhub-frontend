package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.ProjectEntity;
import com.taskhub.ai.common.entity.UserEntity;
import com.taskhub.ai.dto.ProjectCreateDTO;
import com.taskhub.ai.dto.ProjectUpdateDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.*;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.service.ProjectService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import com.taskhub.ai.vo.ProjectVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final ProjectMemberService projectMemberService;
    private final TaskRepository taskRepository;
    private final BugRepository bugRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectVO createProject(ProjectCreateDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        ProjectEntity entity = new ProjectEntity();
        entity.setId(snowflakeIdGenerator.nextId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOwnerId(currentUserId);
        return convertToVO(projectRepository.save(entity));
    }

    @Override
    public List<ProjectVO> listUserProjects() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (currentUser.getIsAdmin() == 1) {
            return projectRepository.findAll().stream()
                    .filter(p -> p.getIsDeleted() == 0)
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
        } else {
            return projectRepository.findByOwnerId(currentUserId)
                    .stream().map(this::convertToVO).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public ProjectVO updateProject(Long id, ProjectUpdateDTO dto) {
        log.info("更新项目请求: id={}, name={}", id, dto.getName());
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("项目不存在, id=" + id));
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (currentUser.getIsAdmin() == 1) {
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return convertToVO(projectRepository.save(entity));
        }
        boolean isOwner = entity.getOwnerId().equals(currentUserId);
        boolean isAdmin = !isOwner && projectMemberService.hasRole(id, currentUserId, "ADMIN");
        if (!isOwner && !isAdmin) {
            throw new BusinessException("权限不足：只有项目所有者或管理员可以修改项目");
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return convertToVO(projectRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        log.info("========== 开始删除项目, id = {} ==========", id);
        ProjectEntity entity = projectRepository.findByIdRaw(id)
                .orElseThrow(() -> new BusinessException("项目不存在, id=" + id));
        if (entity.getIsDeleted() == 1) {
            throw new BusinessException("项目已被删除，无法重复删除");
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (currentUser.getIsAdmin() != 1) {
            boolean isOwner = entity.getOwnerId().equals(currentUserId);
            boolean isAdmin = !isOwner && projectMemberService.hasRole(id, currentUserId, "ADMIN");
            if (!isOwner && !isAdmin) {
                throw new BusinessException("权限不足：只有项目所有者或管理员可以删除项目");
            }
        }
        // 级联删除任务、缺陷、迭代
        taskRepository.findByProjectId(id).forEach(task -> {
            task.setIsDeleted(1);
            taskRepository.save(task);
        });
        bugRepository.findByProjectId(id).forEach(bug -> {
            bug.setIsDeleted(1);
            bugRepository.save(bug);
        });
        sprintRepository.findByProjectId(id).forEach(sprint -> {
            sprint.setIsDeleted(1);
            sprintRepository.save(sprint);
        });
        int updated = projectRepository.softDeleteById(id);
        if (updated == 0) {
            throw new BusinessException("删除失败，项目可能已被删除");
        }
        log.info("项目 {} 删除成功", id);
    }

    @Override
    public Page<ProjectVO> listUserProjects(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (currentUser.getIsAdmin() == 1) {
            return projectRepository.findAll(pageable).map(this::convertToVO);
        } else {
            return projectRepository.findByOwnerId(userId, pageable).map(this::convertToVO);
        }
    }

    @Override
    public ProjectVO getProjectById(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("项目不存在"));
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        boolean isAdmin = currentUser.getIsAdmin() == 1;
        boolean isOwner = entity.getOwnerId().equals(currentUserId);
        boolean isMember = projectMemberService.isMember(id, currentUserId);
        if (!isAdmin && !isOwner && !isMember) {
            throw new BusinessException("无权限查看此项目");
        }
        return convertToVO(entity);
    }

    private ProjectVO convertToVO(ProjectEntity entity) {
        ProjectVO vo = new ProjectVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setName(entity.getName());
        vo.setDescription(entity.getDescription());
        vo.setOwnerId(String.valueOf(entity.getOwnerId()));
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());
        return vo;
    }
}
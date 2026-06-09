package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.SprintEntity;
import com.taskhub.ai.dto.SprintCreateDTO;
import com.taskhub.ai.dto.SprintUpdateDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.SprintRepository;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.service.SprintService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import com.taskhub.ai.vo.SprintVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
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
    public SprintVO createSprint(SprintCreateDTO dto) {
        if (!(isAdmin() || projectMemberService.isMember(dto.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        SprintEntity entity = new SprintEntity();
        entity.setId(idGenerator.nextId());
        entity.setName(dto.getName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setProjectId(dto.getProjectId());
        return convertToVO(sprintRepository.save(entity));
    }

    @Override
    @Transactional
    public SprintVO updateSprint(Long id, SprintUpdateDTO dto) {
        SprintEntity entity = getSprintEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        entity.setName(dto.getName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return convertToVO(sprintRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteSprint(Long id) {
        SprintEntity entity = getSprintEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        entity.setIsDeleted(1);
        sprintRepository.save(entity);
    }

    @Override
    public SprintVO getSprintById(Long id) {
        SprintEntity entity = getSprintEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return convertToVO(entity);
    }

    @Override
    public List<SprintVO> listSprintsByProject(Long projectId) {
        if (!(isAdmin() || projectMemberService.isMember(projectId, SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return sprintRepository.findByProjectId(projectId)
                .stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Page<SprintVO> listSprintsByProjectPage(Long projectId, Pageable pageable) {
        if (!(isAdmin() || projectMemberService.isMember(projectId, SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return sprintRepository.findByProjectId(projectId, pageable).map(this::convertToVO);
    }

    private SprintEntity getSprintEntity(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new BusinessException("迭代不存在"));
    }

    private SprintVO convertToVO(SprintEntity entity) {
        SprintVO vo = new SprintVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setName(entity.getName());
        vo.setStartDate(entity.getStartDate());
        vo.setEndDate(entity.getEndDate());
        vo.setProjectId(String.valueOf(entity.getProjectId()));
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());
        return vo;
    }
}
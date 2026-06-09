package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.BugEntity;
import com.taskhub.ai.dto.BugCreateDTO;
import com.taskhub.ai.dto.BugUpdateDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.BugRepository;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.service.BugService;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import com.taskhub.ai.vo.BugVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
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
    public BugVO createBug(BugCreateDTO dto) {
        if (!(isAdmin() || projectMemberService.isMember(dto.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        BugEntity entity = new BugEntity();
        entity.setId(idGenerator.nextId());
        entity.setTitle(dto.getTitle());
        entity.setSeverity(dto.getSeverity());
        entity.setProjectId(dto.getProjectId());
        return convertToVO(bugRepository.save(entity));
    }

    @Override
    @Transactional
    public BugVO updateBug(Long id, BugUpdateDTO dto) {
        BugEntity entity = getBugEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        entity.setTitle(dto.getTitle());
        entity.setSeverity(dto.getSeverity());
        return convertToVO(bugRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteBug(Long id) {
        BugEntity entity = getBugEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        entity.setIsDeleted(1);
        bugRepository.save(entity);
    }

    @Override
    public BugVO getBugById(Long id) {
        BugEntity entity = getBugEntity(id);
        if (!(isAdmin() || projectMemberService.isMember(entity.getProjectId(), SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return convertToVO(entity);
    }

    @Override
    public List<BugVO> listBugsByProject(Long projectId) {
        if (!(isAdmin() || projectMemberService.isMember(projectId, SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return bugRepository.findByProjectId(projectId)
                .stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Page<BugVO> listBugsByProjectPage(Long projectId, Pageable pageable) {
        if (!(isAdmin() || projectMemberService.isMember(projectId, SecurityUtils.getCurrentUserId()))) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
        return bugRepository.findByProjectId(projectId, pageable).map(this::convertToVO);
    }

    private BugEntity getBugEntity(Long id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new BusinessException("缺陷不存在"));
    }

    private BugVO convertToVO(BugEntity entity) {
        BugVO vo = new BugVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setTitle(entity.getTitle());
        vo.setSeverity(entity.getSeverity());
        vo.setProjectId(String.valueOf(entity.getProjectId()));
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedTime(entity.getUpdatedTime());
        return vo;
    }
}
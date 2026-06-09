package com.taskhub.ai.service.impl;

import com.taskhub.ai.common.entity.ProjectEntity;
import com.taskhub.ai.common.entity.ProjectMemberEntity;
import com.taskhub.ai.common.entity.UserEntity;
import com.taskhub.ai.dto.ProjectMemberAddDTO;
import com.taskhub.ai.dto.ProjectMemberUpdateRoleDTO;
import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.repository.ProjectMemberRepository;
import com.taskhub.ai.repository.ProjectRepository;
import com.taskhub.ai.repository.UserRepository;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.utils.SecurityUtils;
import com.taskhub.ai.utils.SnowflakeIdGenerator;
import com.taskhub.ai.vo.ProjectMemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SnowflakeIdGenerator idGenerator;

    private void checkProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new BusinessException("项目不存在");
        }
    }

    private void checkCurrentUserIsAdminOrOwner(Long projectId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (currentUser.getIsAdmin() == 1) {
            return;
        }
        boolean isOwner = projectRepository.findById(projectId)
                .map(p -> p.getOwnerId().equals(currentUserId))
                .orElse(false);
        if (isOwner) return;
        boolean isAdmin = memberRepository.findByProjectIdAndUserId(projectId, currentUserId)
                .map(m -> "ADMIN".equals(m.getRole()))
                .orElse(false);
        if (!isAdmin) {
            throw new BusinessException("权限不足，需要项目管理员或所有者");
        }
    }

    @Override
    public List<ProjectMemberVO> listProjectMembers(Long projectId) {
        checkProjectExists(projectId);
        return memberRepository.findByProjectId(projectId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectMemberVO addMember(Long projectId, ProjectMemberAddDTO dto) {
        checkProjectExists(projectId);
        checkCurrentUserIsAdminOrOwner(projectId);
        if (memberRepository.existsByProjectIdAndUserId(projectId, dto.getUserId())) {
            throw new BusinessException("用户已是项目成员");
        }
        if (!userRepository.existsById(dto.getUserId())) {
            throw new BusinessException("用户不存在");
        }
        ProjectMemberEntity entity = new ProjectMemberEntity();
        entity.setId(idGenerator.nextId());
        entity.setProjectId(projectId);
        entity.setUserId(dto.getUserId());
        entity.setRole(dto.getRole());
        return convertToVO(memberRepository.save(entity));
    }

    @Override
    @Transactional
    public ProjectMemberVO updateMemberRole(Long projectId, Long userId, ProjectMemberUpdateRoleDTO dto) {
        checkProjectExists(projectId);
        checkCurrentUserIsAdminOrOwner(projectId);
        ProjectMemberEntity entity = memberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new BusinessException("该用户不是项目成员"));
        entity.setRole(dto.getRole());
        return convertToVO(memberRepository.save(entity));
    }

    @Override
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        checkProjectExists(projectId);
        checkCurrentUserIsAdminOrOwner(projectId);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException("项目不存在"));
        if (project.getOwnerId().equals(userId)) {
            throw new BusinessException("不能移除项目所有者");
        }
        memberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public boolean isMember(Long projectId, Long userId) {
        return memberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public boolean hasRole(Long projectId, Long userId, String requiredRole) {
        boolean isOwner = projectRepository.findById(projectId)
                .map(p -> p.getOwnerId().equals(userId))
                .orElse(false);
        if (isOwner) return true;
        return memberRepository.findByProjectIdAndUserId(projectId, userId)
                .map(m -> "ADMIN".equals(m.getRole()) || ("MEMBER".equals(requiredRole) && "MEMBER".equals(m.getRole())))
                .orElse(false);
    }

    private ProjectMemberVO convertToVO(ProjectMemberEntity entity) {
        ProjectMemberVO vo = new ProjectMemberVO();
        vo.setId(String.valueOf(entity.getId()));
        vo.setUserId(String.valueOf(entity.getUserId()));
        vo.setProjectId(String.valueOf(entity.getProjectId()));
        vo.setRole(entity.getRole());
        vo.setCreatedTime(entity.getCreatedTime());
        userRepository.findById(entity.getUserId()).ifPresent(user -> {
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
        });
        return vo;
    }
}
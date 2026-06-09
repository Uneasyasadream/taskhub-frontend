package com.taskhub.ai.service;

import com.taskhub.ai.dto.ProjectMemberAddDTO;
import com.taskhub.ai.dto.ProjectMemberUpdateRoleDTO;
import com.taskhub.ai.vo.ProjectMemberVO;
import java.util.List;

public interface ProjectMemberService {
    List<ProjectMemberVO> listProjectMembers(Long projectId);
    ProjectMemberVO addMember(Long projectId, ProjectMemberAddDTO dto);
    ProjectMemberVO updateMemberRole(Long projectId, Long userId, ProjectMemberUpdateRoleDTO dto);
    void removeMember(Long projectId, Long userId);
    boolean isMember(Long projectId, Long userId);
    boolean hasRole(Long projectId, Long userId, String requiredRole);
}
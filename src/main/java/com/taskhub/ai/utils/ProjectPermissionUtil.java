package com.taskhub.ai.utils;

import com.taskhub.ai.exception.BusinessException;
import com.taskhub.ai.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectPermissionUtil {

    private static ProjectMemberService projectMemberService;

    @Autowired
    public void setProjectMemberService(ProjectMemberService service) {
        ProjectPermissionUtil.projectMemberService = service;
    }

    /**
     * 检查当前用户是否是指定项目的成员（至少是 MEMBER 角色）
     */
    public static void checkMember(Long projectId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isOwner = checkIsOwner(projectId, userId);
        if (isOwner) return;
        if (!projectMemberService.isMember(projectId, userId)) {
            throw new BusinessException("无权限：您不是该项目的成员");
        }
    }

    /**
     * 检查当前用户是否是指定项目的管理员或所有者
     */
    public static void checkAdmin(Long projectId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean isOwner = checkIsOwner(projectId, userId);
        if (isOwner) return;
        if (!projectMemberService.hasRole(projectId, userId, "ADMIN")) {
            throw new BusinessException("权限不足：需要项目管理员权限");
        }
    }

    private static boolean checkIsOwner(Long projectId, Long userId) {
        // 需要注入 ProjectRepository，为简化放在调用处自行判断
        // 建议在具体 Service 中直接调用 repository
        return false;
    }
}
package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.ProjectMemberAddDTO;
import com.taskhub.ai.dto.ProjectMemberUpdateRoleDTO;
import com.taskhub.ai.service.ProjectMemberService;
import com.taskhub.ai.vo.ProjectMemberVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public Result<List<ProjectMemberVO>> listMembers(@PathVariable Long projectId) {
        return Result.success(projectMemberService.listProjectMembers(projectId));
    }

    @PostMapping
    public Result<ProjectMemberVO> addMember(@PathVariable Long projectId,
                                             @Valid @RequestBody ProjectMemberAddDTO dto) {
        return Result.success(projectMemberService.addMember(projectId, dto));
    }

    @PutMapping("/{userId}")
    public Result<ProjectMemberVO> updateMemberRole(@PathVariable Long projectId,
                                                    @PathVariable Long userId,
                                                    @Valid @RequestBody ProjectMemberUpdateRoleDTO dto) {
        return Result.success(projectMemberService.updateMemberRole(projectId, userId, dto));
    }

    @DeleteMapping("/{userId}")
    public Result<Void> removeMember(@PathVariable Long projectId,
                                     @PathVariable Long userId) {
        projectMemberService.removeMember(projectId, userId);
        return Result.success();
    }
}
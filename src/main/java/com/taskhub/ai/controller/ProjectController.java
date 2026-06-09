package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.ProjectCreateDTO;
import com.taskhub.ai.dto.ProjectUpdateDTO;
import com.taskhub.ai.service.ProjectService;
import com.taskhub.ai.vo.ProjectVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public Result<ProjectVO> createProject(@Valid @RequestBody ProjectCreateDTO dto) {
        return Result.success(projectService.createProject(dto));
    }

    @GetMapping
    public Result<List<ProjectVO>> listProjects() {
        return Result.success(projectService.listUserProjects());
    }

    @GetMapping("/page")
    public Result<Page<ProjectVO>> listProjectsPage(Pageable pageable) {
        return Result.success(projectService.listUserProjects(pageable));
    }

    @GetMapping("/{id}")
    public Result<ProjectVO> getProject(@PathVariable Long id) {
        return Result.success(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public Result<ProjectVO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpdateDTO dto) {
        return Result.success(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return Result.success();
    }
}
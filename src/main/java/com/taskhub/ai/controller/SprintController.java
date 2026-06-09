package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.SprintCreateDTO;
import com.taskhub.ai.dto.SprintUpdateDTO;
import com.taskhub.ai.service.SprintService;
import com.taskhub.ai.vo.SprintVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PostMapping
    public Result<SprintVO> createSprint(@Valid @RequestBody SprintCreateDTO dto) {
        return Result.success(sprintService.createSprint(dto));
    }

    @PutMapping("/{id}")
    public Result<SprintVO> updateSprint(@PathVariable Long id, @Valid @RequestBody SprintUpdateDTO dto) {
        return Result.success(sprintService.updateSprint(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SprintVO> getSprint(@PathVariable Long id) {
        return Result.success(sprintService.getSprintById(id));
    }

    @GetMapping
    public Result<List<SprintVO>> listSprintsByProject(@RequestParam Long projectId) {
        return Result.success(sprintService.listSprintsByProject(projectId));
    }

    @GetMapping("/page")
    public Result<Page<SprintVO>> listSprintsByProjectPage(@RequestParam Long projectId, Pageable pageable) {
        return Result.success(sprintService.listSprintsByProjectPage(projectId, pageable));
    }
}
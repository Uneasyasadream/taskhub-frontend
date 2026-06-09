package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.TaskCreateDTO;
import com.taskhub.ai.dto.TaskUpdateDTO;
import com.taskhub.ai.service.TaskService;
import com.taskhub.ai.vo.TaskVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Result<TaskVO> createTask(@Valid @RequestBody TaskCreateDTO dto) {
        return Result.success(taskService.createTask(dto));
    }

    @PutMapping("/{id}")
    public Result<TaskVO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO dto) {
        return Result.success(taskService.updateTask(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TaskVO> getTask(@PathVariable Long id) {
        return Result.success(taskService.getTaskById(id));
    }

    @GetMapping
    public Result<Page<TaskVO>> listTasksByProject(@RequestParam Long projectId, Pageable pageable) {
        return Result.success(taskService.listTasksByProject(projectId, pageable));
    }
}
package com.taskhub.ai.controller;

import com.taskhub.ai.common.api.Result;
import com.taskhub.ai.dto.BugCreateDTO;
import com.taskhub.ai.dto.BugUpdateDTO;
import com.taskhub.ai.service.BugService;
import com.taskhub.ai.vo.BugVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bugs")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;

    @PostMapping
    public Result<BugVO> createBug(@Valid @RequestBody BugCreateDTO dto) {
        return Result.success(bugService.createBug(dto));
    }

    @PutMapping("/{id}")
    public Result<BugVO> updateBug(@PathVariable Long id, @Valid @RequestBody BugUpdateDTO dto) {
        return Result.success(bugService.updateBug(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<BugVO> getBug(@PathVariable Long id) {
        return Result.success(bugService.getBugById(id));
    }

    @GetMapping
    public Result<List<BugVO>> listBugsByProject(@RequestParam Long projectId) {
        return Result.success(bugService.listBugsByProject(projectId));
    }

    @GetMapping("/page")
    public Result<Page<BugVO>> listBugsByProjectPage(@RequestParam Long projectId, Pageable pageable) {
        return Result.success(bugService.listBugsByProjectPage(projectId, pageable));
    }
}
package com.taskhub.ai.service;

import com.taskhub.ai.dto.SprintCreateDTO;
import com.taskhub.ai.dto.SprintUpdateDTO;
import com.taskhub.ai.vo.SprintVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SprintService {
    SprintVO createSprint(SprintCreateDTO dto);
    SprintVO updateSprint(Long id, SprintUpdateDTO dto);
    void deleteSprint(Long id);
    SprintVO getSprintById(Long id);
    List<SprintVO> listSprintsByProject(Long projectId);
    Page<SprintVO> listSprintsByProjectPage(Long projectId, Pageable pageable);
}
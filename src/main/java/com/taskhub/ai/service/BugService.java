package com.taskhub.ai.service;

import com.taskhub.ai.dto.BugCreateDTO;
import com.taskhub.ai.dto.BugUpdateDTO;
import com.taskhub.ai.vo.BugVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BugService {
    BugVO createBug(BugCreateDTO dto);
    BugVO updateBug(Long id, BugUpdateDTO dto);
    void deleteBug(Long id);
    BugVO getBugById(Long id);
    List<BugVO> listBugsByProject(Long projectId);
    Page<BugVO> listBugsByProjectPage(Long projectId, Pageable pageable);
}
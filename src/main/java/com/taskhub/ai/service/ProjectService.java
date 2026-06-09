package com.taskhub.ai.service;

import com.taskhub.ai.dto.ProjectCreateDTO;
import com.taskhub.ai.dto.ProjectUpdateDTO;
import com.taskhub.ai.vo.ProjectVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    ProjectVO createProject(ProjectCreateDTO dto);
    List<ProjectVO> listUserProjects();
    ProjectVO updateProject(Long id, ProjectUpdateDTO dto);
    void deleteProject(Long id);
    Page<ProjectVO> listUserProjects(Pageable pageable);
    ProjectVO getProjectById(Long id);
}
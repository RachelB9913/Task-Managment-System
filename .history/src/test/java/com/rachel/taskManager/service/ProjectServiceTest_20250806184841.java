package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.repository.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectMapper = new ProjectMapper();
        projectService = new ProjectService(projectRepository, projectMapper);
    }

    @Test
    void getAllProjects_ReturnsPageOfProjects() {
        Project p1 = new Project();
        p1.setName("Test Project");
        p1.setDescription("For testing");

        List<Project> projects = List.of(p1);
        Page<Project> projectPage = new PageImpl<>(projects);

        when(projectRepository.findAll(any(PageRequest.class))).thenReturn(projectPage);

        Page<ProjectResponseDTO> result = projectService.getAllProjects(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Project", result.getContent().get(0).getName());
    }
}

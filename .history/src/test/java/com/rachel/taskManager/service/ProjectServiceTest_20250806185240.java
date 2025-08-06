package com.rachel.taskManager.service;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.UserRepository;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.dto.ProjectDTO;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setCognitoSub("abc123");
        user.setMail("user@example.com");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setUser(user);
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

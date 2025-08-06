package com.rachel.taskManager.service;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.UserRepository;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.ArgumentMatchers.any;
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
        public void testCreateProject_Success() {
            ProjectRequestDTO requestDTO = new ProjectRequestDTO();
            requestDTO.setName("New Project");
            requestDTO.setDescription("Some description");

            Project savedProject = new Project();
            savedProject.setId(1L);
            savedProject.setName("New Project");
            savedProject.setDescription("Some description");

            ProjectResponseDTO responseDTO = new ProjectResponseDTO();
            responseDTO.setId(1L);
            responseDTO.setName("New Project");
            responseDTO.setDescription("Some description");

            when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
            when(projectMapper.toDTO(savedProject)).thenReturn(responseDTO);

            ProjectResponseDTO result = projectService.createProject(requestDTO, user);

            assertNotNull(result);
            assertEquals("New Project", result.getName());
            assertEquals("Some description", result.getDescription());

            verify(projectRepository).save(any(Project.class));
            verify(projectMapper).toDTO(savedProject);
        }



}

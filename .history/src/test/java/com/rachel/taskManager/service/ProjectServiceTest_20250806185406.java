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
import org.springframework.data.domain.Page;

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
        public void testCreateProject_Success() {
            ProjectRequestDTO requestDTO = new ProjectRequestDTO();
            requestDTO.setName("New Project");
            requestDTO.setDescription("Some description");

            ProjectResponseDTO responseDTO = new ProjectResponseDTO();
            responseDTO.setId(1L);
            responseDTO.setName("New Project");
            responseDTO.setDescription("Some description");

            when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));
            when(projectMapper.fromDTO(requestDTO)).thenReturn(project);
            when(projectRepository.save(project)).thenReturn(project);
            when(projectMapper.toDTO(project)).thenReturn(responseDTO);

            ProjectResponseDTO result = projectService.createProject(requestDTO, "abc123");

            assertNotNull(result);
            assertEquals("New Project", result.getName());
            verify(userRepository).findByCognitoSub("abc123");
            verify(projectRepository).save(project);
            verify(projectMapper).toDTO(project);
        }

}

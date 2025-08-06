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

import java.util.List;
import java.util.Set;

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

        @Test
        public void testGetProjectsForUser_Success() {
            // Arrange
            Set<Project> projects = Set.of(
                new Project(1L, "Project A", "Desc A"),
                new Project(2L, "Project B", "Desc B")
            );

            user.setProjects(projects);

            when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));
            when(projectMapper.toDTO(any(Project.class)))
                .thenAnswer(invocation -> {
                    Project p = invocation.getArgument(0);
                    return new ProjectResponseDTO();
                });

            // Act
            List<ProjectResponseDTO> result = projectService.getProjectsForUser("abc123");

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            List<String> projectNames = result.stream().map(ProjectResponseDTO::getName).toList();
            assertTrue(projectNames.contains("Project A"));
            assertTrue(projectNames.contains("Project B"));

            verify(userRepository).findByCognitoSub("abc123");
            verify(projectMapper, times(2)).toDTO(any(Project.class));
        }





}

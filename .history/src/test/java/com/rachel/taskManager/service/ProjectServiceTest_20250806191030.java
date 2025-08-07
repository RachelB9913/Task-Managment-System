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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        public void testGetAllProjects_Success() {
            Project project1 = new Project();
            project1.setId(1L);
            project1.setName("Project One");
            project1.setDescription("Desc One");

            Project project2 = new Project();
            project2.setId(2L);
            project2.setName("Project Two");
            project2.setDescription("Desc Two");

            ProjectResponseDTO dto1 = new ProjectResponseDTO();
            dto1.setId(1L);
            dto1.setName("Project One");
            dto1.setDescription("Desc One");

            ProjectResponseDTO dto2 = new ProjectResponseDTO();
            dto2.setId(2L);
            dto2.setName("Project Two");
            dto2.setDescription("Desc Two");

            Pageable pageable = PageRequest.of(0, 10);
            Page<Project> projectPage = new PageImpl<>(List.of(project1, project2));

            when(projectRepository.findAll(pageable)).thenReturn(projectPage);
            when(projectMapper.toDTO(project1)).thenReturn(dto1);
            when(projectMapper.toDTO(project2)).thenReturn(dto2);

            Page<ProjectResponseDTO> result = projectService.getAllProjects(pageable);

            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertEquals("Project One", result.getContent().get(0).getName());

            verify(projectRepository).findAll(pageable);
            verify(projectMapper).toDTO(project1);
            verify(projectMapper).toDTO(project2);
        }

        @Test
        public void testGetProjectsForUser_Success() {
            String userSub = "abc-123";

            User user = new User();
            user.setCognitoSub(userSub);

            Project project = new Project();
            project.setId(1L);
            project.setName("My Project");
            project.setDescription("Some desc");

            ProjectResponseDTO dto = new ProjectResponseDTO();
            dto.setId(1L);
            dto.setName("My Project");
            dto.setDescription("Some desc");

            when(userRepository.findById(userSub)).thenReturn(Optional.of(user));
            when(projectRepository.findByUser(user)).thenReturn(List.of(project));
            when(projectMapper.toDTO(project)).thenReturn(dto);

            List<ProjectResponseDTO> result = projectService.getProjectsForUser(userSub);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("My Project", result.get(0).getName());

            verify(userRepository).findById(userSub);
            verify(projectRepository).findByUser(user);
            verify(projectMapper).toDTO(project);
        }




        





}

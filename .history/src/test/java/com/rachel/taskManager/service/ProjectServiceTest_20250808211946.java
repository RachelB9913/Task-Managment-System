package com.rachel.taskManager.service;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.TaskRepository;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private TaskRepository taskRepository;

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

        project = new Project(1L, "Test Project", "Test Description");
        project.setUser(user);
    }

    @Test
    public void testCreateProject_Success() {
        ProjectRequestDTO requestDTO = new ProjectRequestDTO();
        requestDTO.setName("New Project");
        requestDTO.setDescription("Some description");

        Project savedProject = new Project(1L, "New Project", "Some description");

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
        Project project1 = new Project(1L, "Project One", "Desc One");
        Project project2 = new Project(2L, "Project Two", "Desc Two");

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
    public void testGetProjectById_Success() {
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectMapper.toDTO(project)).thenReturn(new ProjectResponseDTO());

        ProjectResponseDTO result = projectService.getProjectById(projectId, user);

        assertNotNull(result);
        verify(projectRepository).findById(projectId);
        verify(projectMapper).toDTO(project);
    }

    @Test
    public void testGetProjectById_UnauthorizedUser() {
        Long projectId = 1L;
        User otherUser = new User();
        otherUser.setCognitoSub("xyz789");
        otherUser.setMail("hacker@example.com");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(SecurityException.class, () -> {
            projectService.getProjectById(projectId, otherUser);
        });

        verify(projectRepository).findById(projectId);
        verify(projectMapper, never()).toDTO(any());
    }

    @Test
    public void testUpdateProject_Success() {
        Long projectId = 1L;
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Updated Name");
        dto.setDescription("Updated Description");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toDTO(project)).thenReturn(new ProjectResponseDTO());

        ProjectResponseDTO result = projectService.updateProject(projectId, dto, user);

        assertNotNull(result);
        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(project);
        verify(projectMapper).toDTO(project);

        assertEquals("Updated Name", project.getName());
        assertEquals("Updated Description", project.getDescription());
    }

    @Test
    public void testUpdateProject_UnauthorizedUser() {
        Long projectId = 1L;
        User otherUser = new User();
        otherUser.setCognitoSub("xyz789");
        otherUser.setMail("unauthorized@example.com");

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("New Name");
        dto.setDescription("New Desc");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(SecurityException.class, () -> {
            projectService.updateProject(projectId, dto, otherUser);
        });

        verify(projectRepository).findById(projectId);
        verify(projectRepository, never()).save(any());
        verify(projectMapper, never()).toDTO(any());
    }


    @Test
    public void testDeleteProject_Success() {
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId, user);

        verify(projectRepository).findById(projectId);
        verify(projectRepository).delete(project);
    }


    @Test
    public void testDeleteProject_NotFound() {
        Long projectId = 99L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            projectService.deleteProject(projectId, user);
        });

        verify(projectRepository).findById(projectId);
        verify(projectRepository, never()).delete(any());
    }

}

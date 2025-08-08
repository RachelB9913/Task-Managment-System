package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProject_ReturnsProjectResponseDTO() {
        Long projectId = 1L;
        User user = mock(User.class);
        ProjectResponseDTO dto = new ProjectResponseDTO();
        when(projectService.getProjectById(eq(projectId), eq(user))).thenReturn(dto);

        ResponseEntity<ProjectResponseDTO> response = projectController.getProject(projectId, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void createProject_ReturnsCreatedProject() {
        ProjectRequestDTO request = new ProjectRequestDTO();
        User user = mock(User.class);
        ProjectResponseDTO created = new ProjectResponseDTO();
        when(projectService.createProject(eq(request), eq(user))).thenReturn(created);

        ResponseEntity<ProjectResponseDTO> response = projectController.createProject(request, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(created);
    }

    @Test
    void updateProject_ReturnsUpdatedProject() {
        Long projectId = 1L;
        ProjectRequestDTO request = new ProjectRequestDTO();
        User user = mock(User.class);
        ProjectResponseDTO updated = new ProjectResponseDTO();
        when(projectService.updateProject(eq(projectId), eq(request), eq(user))).thenReturn(updated);

        ResponseEntity<ProjectResponseDTO> response = projectController.updateProject(projectId, request, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
    }

    @Test
    void deleteProject_ReturnsNoContent() {
        Long projectId = 1L;
        User user = mock(User.class);
        doNothing().when(projectService).deleteProject(projectId, user);

        ResponseEntity<Void> response = projectController.deleteProject(projectId, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(String.format("Project %d deleted successfully.", projectId));
        verify(projectService, times(1)).deleteProject(projectId, user);
    }
}
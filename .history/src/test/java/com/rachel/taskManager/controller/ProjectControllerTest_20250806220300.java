package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.controller.ProjectController;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.ProjectWithTaskSummaryDTO;
import com.rachel.taskManager.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectResponseDTO responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new ProjectResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("My Project");
        responseDTO.setDescription("Some description");
    }

    @Test
    public void testGetAllProjects() throws Exception {
        Page<ProjectResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(projectService.getAllProjects(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/projects/all?page=0&size=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("My Project"));
    }

    // @Test
    // public void testGetProjectsForCurrentUser() throws Exception {
    //     when(projectService.getProjectsForUser(anyString())).thenReturn(List.of(responseDTO));

    //     mockMvc.perform(get("/api/projects")
    //             .header("X-User-Sub", "abc123")) // simulate sub
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$[0].id").value(1L));
    // }

    // @Test
    // public void testGetProjectsWithTasks() throws Exception {
    //     ProjectWithTaskSummaryDTO projectWithTasks = new ProjectWithTaskSummaryDTO();
    //     projectWithTasks.setId(1L);
    //     projectWithTasks.setName("My Project");
    //     projectWithTasks.setDescription("With tasks");
    //     projectWithTasks.setTasks(List.of()); // no tasks

    //     Page<ProjectWithTaskSummaryDTO> page = new PageImpl<>(List.of(projectWithTasks));
    //     when(projectService.getProjectsWithTasks(anyString(), any(Pageable.class))).thenReturn(page);

    //     mockMvc.perform(get("/api/projects/tasks?page=0&size=5")
    //             .header("X-User-Sub", "abc123"))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$.content[0].name").value("My Project"));
    // }

    @Test
    public void testCreateProject() throws Exception {
        ProjectRequestDTO requestDTO = new ProjectRequestDTO();
        requestDTO.setName("New Project");
        requestDTO.setDescription("Cool stuff");

        when(projectService.createProject(any(ProjectRequestDTO.class), any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/projects")
                .header("X-User-Sub", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("My Project"));
    }

    @Test
    public void testUpdateProject() throws Exception {
        ProjectRequestDTO requestDTO = new ProjectRequestDTO();
        requestDTO.setName("Updated");
        requestDTO.setDescription("Updated desc");

        when(projectService.updateProject(eq(1L), any(ProjectRequestDTO.class), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/api/projects/1")
                .header("X-User-Sub", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/projects/1"))
            .andExpect(status().isOk());
    }
}

package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProjectController.class)
class ProjectControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createProject_authenticated_returnsCreated() throws Exception {
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Test Project");
        request.setDescription("Test Desc");
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Test Project");
        response.setDescription("Test Desc");
        when(projectService.createProject(any(ProjectRequestDTO.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void createProject_unauthenticated_returnsUnauthorized() throws Exception {
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Test Project");
        request.setDescription("Test Desc");
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    // Add more tests for GET, PUT, DELETE as needed, using @WithMockUser for authenticated scenarios
}

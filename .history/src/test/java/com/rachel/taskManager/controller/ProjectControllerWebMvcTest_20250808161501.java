
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @MockBean
    private com.rachel.taskManager.service.UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProject_authenticated_returnsCreated() throws Exception {
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Test Project");
        request.setDescription("Test Desc");
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Test Project");
        response.setDescription("Test Desc");
        when(projectService.createProject(any(ProjectRequestDTO.class), any())).thenReturn(response);

        // Mock userService.getOrCreateUserFromToken to return a User for @CurrentUser
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);

        // Create a minimal JWT and set it in the security context
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "testuser")
                .build();
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
        )
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

    @Test
    void getProject_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(projectId);
        response.setName("Test Project");
        when(projectService.getProjectById(eq(projectId), any())).thenReturn(response);
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(get("/api/projects/{projectId}", projectId)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void updateProject_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Project");
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(projectId);
        response.setName("Updated Project");
        when(projectService.updateProject(eq(projectId), any(), any())).thenReturn(response);
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(put("/api/projects/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void deleteProject_authenticated_returnsNoContent() throws Exception {
        Long projectId = 1L;
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        Mockito.doNothing().when(projectService).deleteProject(eq(projectId), any(com.rachel.taskManager.model.User.class));
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(delete("/api/projects/{projectId}", projectId)
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProject_unauthenticated_returnsUnauthorized() throws Exception {
        Long projectId = 1L;
        mockMvc.perform(get("/api/projects/{projectId}", projectId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProject_notFound_returnsNotFound() throws Exception {
        Long projectId = 999L;
        when(projectService.getProjectById(eq(projectId), any())).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(get("/api/projects/{projectId}", projectId)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProject_unauthenticated_returnsUnauthorized() throws Exception {
        Long projectId = 1L;
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Project");
        mockMvc.perform(put("/api/projects/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProject_notFound_returnsNotFound() throws Exception {
        Long projectId = 999L;
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Project");
        when(projectService.updateProject(eq(projectId), any(), any())).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(put("/api/projects/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProject_unauthenticated_returnsUnauthorized() throws Exception {
        Long projectId = 1L;
        mockMvc.perform(delete("/api/projects/{projectId}", projectId)
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void deleteProject_notFound_returnsNotFound() throws Exception {
        Long projectId = 999L;
        com.rachel.taskManager.model.User mockUser = new com.rachel.taskManager.model.User();
        Mockito.doThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)).when(projectService).deleteProject(eq(projectId), any(com.rachel.taskManager.model.User.class));
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser);
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(delete("/api/projects/{projectId}", projectId)
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isNotFound());
    }
}

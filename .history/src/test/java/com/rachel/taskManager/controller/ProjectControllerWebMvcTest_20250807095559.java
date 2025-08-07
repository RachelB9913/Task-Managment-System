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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(request1 -> {
                    request1.setUserPrincipal(jwtAuth);
                    request1.setAttribute("org.springframework.security.core.context.SecurityContextHolderAwareRequestWrapper.AUTHENTICATION", jwtAuth);
                    return request1;
                }))
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

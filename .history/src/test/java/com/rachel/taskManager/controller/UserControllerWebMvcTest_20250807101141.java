package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectWithTaskSummaryDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Jwt jwt() {
        return Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
    }

    private User mockUser() {
        User user = new User();
        user.setCognitoSub("testuser");
        return user;
    }

    @Test
    void getCurrentUser_authenticated_returnsOk() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userService.getCurrentUserSummary(any())).thenReturn(userDTO);
        mockMvc.perform(get("/api/users/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk());
    }

    @Test
    void getProjectsForCurrentUser_authenticated_returnsOk() throws Exception {
        ProjectWithTaskSummaryDTO dto = new ProjectWithTaskSummaryDTO();
        Page<ProjectWithTaskSummaryDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(userService.getProjectsWithTasks(any(), any())).thenReturn(page);
        mockMvc.perform(get("/api/users/me/projects?page=0&size=10")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}

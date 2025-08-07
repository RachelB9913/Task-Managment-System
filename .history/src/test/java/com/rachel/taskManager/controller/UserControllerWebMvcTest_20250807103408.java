package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.ProjectWithTaskSummaryDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

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

    // @Autowired
    // private ObjectMapper objectMapper;

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
        userDTO.setMail("test@mail.com");
        userDTO.setAdmin(true);
        when(userService.getCurrentUserSummary(any())).thenReturn(userDTO);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(get("/api/users/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mail").value("test@mail.com"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void getProjectsForCurrentUser_authenticated_returnsOk() throws Exception {
        ProjectWithTaskSummaryDTO dto = new ProjectWithTaskSummaryDTO();
        dto.setId(123L);
        dto.setName("Project X");
        Page<ProjectWithTaskSummaryDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(userService.getProjectsWithTasks(any(), any())).thenReturn(page);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(get("/api/users/me/projects?page=0&size=10")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(123L))
                .andExpect(jsonPath("$.content[0].name").value("Project X"));
    }

    @Test
    void getCurrentUser_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getCurrentUser_notFound_returnsNotFound() throws Exception {
        when(userService.getCurrentUserSummary(any()))
            .thenThrow(new NoSuchElementException("User not found"));
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());

        mockMvc.perform(get("/api/users/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

}

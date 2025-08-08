package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

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

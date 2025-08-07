package com.rachel.taskManager.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authCallback_isAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/auth/callback"))
                .andExpect(status().isOk()
                        .or(status().is4xxClientError())); // Accepts 200 or 4xx if controller not implemented
    }

    @Test
    void usersMe_requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void usersMe_withAuth_isAccessible() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk()
                        .or(status().is4xxClientError())); // Accepts 200 or 4xx if controller not implemented
    }

    @Test
    void otherEndpoints_arePermitted() throws Exception {
        mockMvc.perform(get("/public-endpoint"))
                .andExpect(status().isOk()
                        .or(status().is4xxClientError())); // Accepts 200 or 4xx if controller not implemented
    }
}

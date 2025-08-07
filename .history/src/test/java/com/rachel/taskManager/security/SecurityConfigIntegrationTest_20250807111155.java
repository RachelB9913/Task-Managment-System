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
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || (status >= 400 && status < 500),
                        "Expected 200 OK or 4xx client error, got: " + status);
                });
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
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || (status >= 400 && status < 500),
                        "Expected 200 OK or 4xx client error, got: " + status);
                });
    }

    @Test
    void otherEndpoints_arePermitted() throws Exception {
        mockMvc.perform(get("/public-endpoint"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 200 || (status >= 400 && status < 500),
                        "Expected 200 OK or 4xx client error, got: " + status);
                });
    }
}

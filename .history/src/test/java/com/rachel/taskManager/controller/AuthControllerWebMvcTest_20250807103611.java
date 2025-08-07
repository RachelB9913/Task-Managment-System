package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.CognitoTokenResponse;
import com.rachel.taskManager.service.CognitoAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CognitoAuthService cognitoAuthService;

    @Test
    void handleCallback_success_returnsTokens() throws Exception {
        CognitoTokenResponse tokens = new CognitoTokenResponse();
        tokens.setAccessToken("access");
        tokens.setIdToken("id");
        tokens.setRefreshToken("refresh");
        when(cognitoAuthService.exchangeCodeForTokens(anyString())).thenReturn(tokens);

        mockMvc.perform(get("/auth/callback?code=abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.idToken").value("id"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"));
    }

    @Test
    void handleCallback_failure_returns500() throws Exception {
        when(cognitoAuthService.exchangeCodeForTokens(anyString())).thenReturn(null);
        mockMvc.perform(get("/auth/callback?code=fail"))
                .andExpect(status().isInternalServerError());
    }
}

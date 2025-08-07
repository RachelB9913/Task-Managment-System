package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.CognitoTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CognitoAuthServiceTest {

    @Mock private RestTemplate restTemplate;

    private CognitoAuthService cognitoAuthService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        String clientId = "test-client";
        String redirectUri = "http://localhost:8080/callback";
        String tokenUri = "http://mock-cognito/token";

        cognitoAuthService = new CognitoAuthService(restTemplate, clientId, redirectUri, tokenUri);
    }

    @Test
    public void testExchangeCodeForTokens_Success() {
        String code = "valid-code";
        CognitoTokenResponse mockResponse = new CognitoTokenResponse();
        mockResponse.setIdToken("id-token");

        ResponseEntity<CognitoTokenResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://mock-cognito/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CognitoTokenResponse.class)
        )).thenReturn(responseEntity);

        CognitoTokenResponse result = cognitoAuthService.exchangeCodeForTokens(code);

        assertNotNull(result);
        assertEquals("id-token", result.getIdToken());
    }
}


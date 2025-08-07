package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.CognitoTokenResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CognitoAuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CognitoAuthService cognitoAuthService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create the real service (not mocked)
        cognitoAuthService = new CognitoAuthService();

        // Inject environment properties manually
        ReflectionTestUtils.setField(cognitoAuthService, "clientId", "test-client");
        ReflectionTestUtils.setField(cognitoAuthService, "redirectUri", "http://localhost:8080/callback");
        ReflectionTestUtils.setField(cognitoAuthService, "tokenUri", "http://mock-cognito/token");

        // Inject the mocked RestTemplate into the private final field
        ReflectionTestUtils.setField(cognitoAuthService, "restTemplate", restTemplate);
    }

    @Test
    public void testExchangeCodeForTokens_Success() {
        String code = "valid-code";

        CognitoTokenResponse mockResponse = new CognitoTokenResponse();
        mockResponse.setIdToken("mock-id-token");

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
        assertEquals("mock-id-token", result.getIdToken());

        verify(restTemplate).exchange(
                eq("http://mock-cognito/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CognitoTokenResponse.class)
        );
    }

    @Test
    public void testExchangeCodeForTokens_Failure_ThrowsException() {
        String code = "bad-code";

        when(restTemplate.exchange(
                eq("http://mock-cognito/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CognitoTokenResponse.class)
        )).thenThrow(new RuntimeException("Token request failed"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cognitoAuthService.exchangeCodeForTokens(code);
        });

        assertEquals("Token request failed", ex.getMessage());
        verify(restTemplate).exchange(
                eq("http://mock-cognito/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CognitoTokenResponse.class)
        );
    }

}

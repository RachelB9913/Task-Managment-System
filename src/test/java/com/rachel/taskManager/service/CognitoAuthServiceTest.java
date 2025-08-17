package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.CognitoTokenResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import com.rachel.taskManager.config.CognitoProperties;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CognitoAuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CognitoAuthService cognitoAuthService;
    private CognitoProperties cognitoProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cognitoProperties = new CognitoProperties();
        cognitoProperties.setClientId("test-client");
        cognitoProperties.setRedirectUri("http://localhost:8080/callback");
        cognitoProperties.setTokenUri("http://mock-cognito/token");

        cognitoAuthService = new CognitoAuthService(cognitoProperties);
        // Inject the mocked RestTemplate into the private final field
        org.springframework.test.util.ReflectionTestUtils.setField(cognitoAuthService, "restTemplate", restTemplate);
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

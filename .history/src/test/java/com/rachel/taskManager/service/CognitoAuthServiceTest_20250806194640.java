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

    @InjectMocks
    private CognitoAuthService cognitoAuthService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CognitoTokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject mock RestTemplate (normally it's final, so change the code if needed to inject)
        cognitoAuthService = spy(new CognitoAuthService());
        doReturn(restTemplate).when(cognitoAuthService).getRestTemplate();
        
        // Set required values via reflection or setters if made available
        cognitoAuthService.clientId = "test-client";
        cognitoAuthService.redirectUri = "http://localhost:8080/callback";
        cognitoAuthService.tokenUri = "http://mock-cognito/token";
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

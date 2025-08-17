package com.rachel.taskManager.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rachel.taskManager.dto.CognitoTokenResponse;

import com.rachel.taskManager.config.CognitoProperties;


@Service
public class CognitoAuthService {
    // This service handles the authentication with AWS Cognito, specifically exchanging an authorization code for tokens.

    private final CognitoProperties cognitoProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public CognitoAuthService(CognitoProperties cognitoProperties) {
        this.cognitoProperties = cognitoProperties;
    }

    public CognitoTokenResponse exchangeCodeForTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", cognitoProperties.getClientId());
        body.add("redirect_uri", cognitoProperties.getRedirectUri());
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<CognitoTokenResponse> response = restTemplate.exchange(
            cognitoProperties.getTokenUri(),
            HttpMethod.POST,
            request,
            CognitoTokenResponse.class
        );

        System.out.println(">>> Token response body: " + response.getBody().getIdToken());

        return response.getBody();
    }
}
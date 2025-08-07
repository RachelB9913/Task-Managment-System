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

import org.springframework.beans.factory.annotation.Value;


@Service
public class CognitoAuthService {

    // @Value("${cognito.client-id}") String clientId;

    // @Value("${cognito.redirect-uri}")
    // private String redirectUri;

    // @Value("${cognito.token-uri}")
    // private String tokenUri;
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String redirectUri;
    private final String tokenUri;

    public CognitoAuthService(
        RestTemplate restTemplate,
        @Value("${cognito.clientId}") String clientId,
        @Value("${cognito.redirectUri}") String redirectUri,
        @Value("${cognito.tokenUri}") String tokenUri
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
    }

    // private final RestTemplate restTemplate = new RestTemplate();

    public CognitoTokenResponse exchangeCodeForTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<CognitoTokenResponse> response = restTemplate.exchange(
            tokenUri,
            HttpMethod.POST,
            request,
            CognitoTokenResponse.class
        );

        System.out.println(">>> Token response body: " + response.getBody().getIdToken());

        return response.getBody();
    }
}
package com.rachel.taskManager.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class CognitoAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String clientId = "YOUR_CLIENT_ID";
    private final String clientSecret = ""; // leave empty if no secret
    private final String redirectUri = "http://localhost:8080/auth/callback";
    private final String tokenEndpoint = "https://eu-north-1lbrgh68wz.auth.eu-north-1.amazoncognito.com/oauth2/token";

    public Map<String, Object> exchangeCodeForTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        if (!clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody(); // contains id_token, access_token, etc.
    }
}

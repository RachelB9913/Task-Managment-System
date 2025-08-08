package com.rachel.taskManager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rachel.taskManager.dto.CognitoTokenResponse;
import com.rachel.taskManager.service.CognitoAuthService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    // A class to handle authentication-related endpoints, specifically for OAuth callbacks.
    // This controller will handle the OAuth callback from Cognito and exchange the code for tokens.
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final CognitoAuthService cognitoAuthService;

    @GetMapping("/callback")
    public ResponseEntity<CognitoTokenResponse> handleCallback(@RequestParam("code") String code) {
        logger.info("Received OAuth callback with code: {}", code);
        CognitoTokenResponse tokens = cognitoAuthService.exchangeCodeForTokens(code);
        if (tokens == null) {
            logger.error("Failed to exchange code for tokens");
            return ResponseEntity.status(500).body(null);
        }
        logger.info("Successfully exchanged code for tokens"); //: {}", tokens);
        return ResponseEntity.ok(tokens);
    }
}

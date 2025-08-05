package com.rachel.taskManager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rachel.taskManager.dto.CognitoTokenResponse;
import com.rachel.taskManager.service.CognitoAuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CognitoAuthService cognitoAuthService;

    @GetMapping("/callback")
    public ResponseEntity<CognitoTokenResponse> handleCallback(@RequestParam("code") String code) {
        CognitoTokenResponse tokens = cognitoAuthService.exchangeCodeForTokens(code);
        return ResponseEntity.ok(tokens);
    }
}

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
        System.out.println(">>> Received code: " + code);
        CognitoTokenResponse tokens = cognitoAuthService.exchangeCodeForTokens(code);
        System.out.println(">>> Received tokens: " + tokens);
        return ResponseEntity.ok(tokens);
    }
}

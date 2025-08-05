package com.rachel.taskManager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rachel.taskManager.service.CognitoAuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CognitoAuthService cognitoAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> handleCognitoCallback(@RequestParam("code") String code) {
        Map<String, Object> tokens = cognitoAuthService.exchangeCodeForTokens(code);
        return ResponseEntity.ok(tokens);
    }
}
 
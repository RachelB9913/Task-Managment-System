package com.rachel.taskManager.util;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

public class JwtUtils {
    public static String extractSub(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaimAsString("sub");
    }
}
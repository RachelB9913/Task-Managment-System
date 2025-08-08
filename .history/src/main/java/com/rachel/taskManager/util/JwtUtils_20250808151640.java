package com.rachel.taskManager.util;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

public class JwtUtils {
    // This utility class provides methods to extract information from JWT tokens.

    public static String extractSub(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaimAsString("sub");
    }
    
}
package com.rachel.taskManager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // This configuration class sets up security for the application, including OAuth2 resource server support.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/callback").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/users/me").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var groupsConverter = new JwtGrantedAuthoritiesConverter();
        groupsConverter.setAuthoritiesClaimName("cognito:groups");
        groupsConverter.setAuthorityPrefix("ROLE_");

        var jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var auths = new java.util.ArrayList<org.springframework.security.core.GrantedAuthority>();

            // Default role for any authenticated user
            auths.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));

            // Add roles from Cognito groups (e.g., ADMIN)
            auths.addAll(groupsConverter.convert(jwt));

            return auths;
        });
        return jwtAuthConverter;
    }

}


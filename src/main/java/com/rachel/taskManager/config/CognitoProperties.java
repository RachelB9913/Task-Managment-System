package com.rachel.taskManager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;


@ConfigurationProperties(prefix = "cognito")
@Data
public class CognitoProperties {
    @Value("${COGNITO_REGION:#{null}}")
    private String region;
    @Value("${COGNITO_USER_POOL_ID:#{null}}")
    private String userPoolId;
    @Value("${COGNITO_CLIENT_ID:#{null}}")
    private String clientId;
    @Value("${COGNITO_DOMAIN:#{null}}")
    private String domain;       // e.g., your-domain.auth.eu-north-1.amazoncognito.com
    @Value("${COGNITO_REDIRECT_URI:#{null}}")
    private String redirectUri;  // e.g., http://localhost:8080/auth/callback
    @Value("${COGNITO_TOKEN_URI:#{null}}")
    private String tokenUri;     // optional: if you want to override
    @Value("${COGNITO_ISSUER_URI:#{null}}")
    private String issuerUri;    // https://cognito-idp.<region>.amazonaws.com/<userPoolId>

}

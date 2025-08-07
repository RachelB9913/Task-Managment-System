package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CognitoTokenResponseTest {
    @Test
    void testNoArgsConstructor() {
        CognitoTokenResponse dto = new CognitoTokenResponse();
        assertNotNull(dto);
    }
}

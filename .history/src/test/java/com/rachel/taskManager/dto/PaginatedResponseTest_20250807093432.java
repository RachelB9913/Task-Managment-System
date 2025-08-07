package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaginatedResponseTest {
    @Test
    void testNoArgsConstructor() {
        PaginatedResponse<String> dto = new PaginatedResponse<>();
        assertNotNull(dto);
    }
}

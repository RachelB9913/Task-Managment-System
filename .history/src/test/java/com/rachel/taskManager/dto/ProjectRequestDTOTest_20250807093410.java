package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectRequestDTOTest {
    @Test
    void testNoArgsConstructor() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        assertNotNull(dto);
    }
}

package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectResponseDTOTest {
    @Test
    void testNoArgsConstructor() {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        assertNotNull(dto);
    }
}

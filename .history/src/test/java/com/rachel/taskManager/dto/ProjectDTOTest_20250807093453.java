package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectDTOTest {
    @Test
    void testNoArgsConstructor() {
        ProjectDTO dto = new ProjectDTO();
        assertNotNull(dto);
    }
}

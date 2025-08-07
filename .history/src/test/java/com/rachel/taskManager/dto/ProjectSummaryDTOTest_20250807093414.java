package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectSummaryDTOTest {
    @Test
    void testNoArgsConstructor() {
        ProjectSummaryDTO dto = new ProjectSummaryDTO();
        assertNotNull(dto);
    }
}

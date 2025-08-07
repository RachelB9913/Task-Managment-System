package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectWithTaskSummaryDTOTest {
    @Test
    void testNoArgsConstructor() {
        ProjectWithTaskSummaryDTO dto = new ProjectWithTaskSummaryDTO();
        assertNotNull(dto);
    }
}

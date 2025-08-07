package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskSummaryDTOTest {
    @Test
    void testNoArgsConstructor() {
        TaskSummaryDTO dto = new TaskSummaryDTO();
        assertNotNull(dto);
    }
}

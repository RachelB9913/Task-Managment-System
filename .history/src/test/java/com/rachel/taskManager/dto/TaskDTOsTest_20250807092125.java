package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");
        dto.setStatus("in_progress");
        dto.setProjectId(123L);

        assertEquals("Test Title", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals("in_progress", dto.getStatus());
        assertEquals(123L, dto.getProjectId());
    }

    @Test
    void testNoArgsConstructor() {
        TaskRequestDTO dto = new TaskRequestDTO();
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getStatus());
        assertNull(dto.getProjectId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaskRequestDTO dto1 = new TaskRequestDTO();
        dto1.setTitle("A");
        dto1.setDescription("B");
        dto1.setStatus("todo");
        dto1.setProjectId(1L);

        TaskRequestDTO dto2 = new TaskRequestDTO();
        dto2.setTitle("A");
        dto2.setDescription("B");
        dto2.setStatus("todo");
        dto2.setProjectId(1L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}

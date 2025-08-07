package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(10L);
        dto.setTitle("Task Title");
        dto.setDescription("Task Desc");
        dto.setStatus("done");

        assertEquals(10L, dto.getId());
        assertEquals("Task Title", dto.getTitle());
        assertEquals("Task Desc", dto.getDescription());
        assertEquals("done", dto.getStatus());
    }

    @Test
    void testNoArgsConstructor() {
        TaskResponseDTO dto = new TaskResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        TaskResponseDTO dto1 = new TaskResponseDTO();
        dto1.setId(1L);
        dto1.setTitle("A");
        dto1.setDescription("B");
        dto1.setStatus("todo");

        TaskResponseDTO dto2 = new TaskResponseDTO();
        dto2.setId(1L);
        dto2.setTitle("A");
        dto2.setDescription("B");
        dto2.setStatus("todo");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}

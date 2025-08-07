package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskDTOsTest {

    @Test
    void testGettersAndSettersRequest() {
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
    void testNoArgsConstructorRequest() {
        TaskRequestDTO dto = new TaskRequestDTO();
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getStatus());
        assertNull(dto.getProjectId());
    }

    @Test
    void testEqualsAndHashCodeRequest() {
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

    @Test
    void testGettersAndSettersResponse() {
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
    void testNoArgsConstructorResponse() {
        TaskResponseDTO dto = new TaskResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getStatus());
    }

    @Test
    void testEqualsAndHashCodeResponse() {
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

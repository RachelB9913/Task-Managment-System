package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDTOsTest {

    @Test
    void testProjectRequestDTO() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Project X");
        dto.setDescription("A test project");

        assertEquals("Project X", dto.getName());
        assertEquals("A test project", dto.getDescription());
    }

    @Test
    void testProjectSummaryDTO() {
        ProjectSummaryDTO dto = new ProjectSummaryDTO();
        dto.setId(1L);
        dto.setName("Summary Project");
        dto.setDescription("Summary description");

        assertEquals(1L, dto.getId());
        assertEquals("Summary Project", dto.getName());
        assertEquals("Summary description", dto.getDescription());
    }

    @Test
    void testProjectResponseDTO() {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(2L);
        dto.setName("Response Project");
        dto.setDescription("Response description");

        // Test with empty tasks
        dto.setTasks(Collections.emptyList());
        assertEquals(2L, dto.getId());
        assertEquals("Response Project", dto.getName());
        assertEquals("Response description", dto.getDescription());
        assertNotNull(dto.getTasks());
        assertTrue(dto.getTasks().isEmpty());

        // Test with tasks
        TaskResponseDTO task1 = new TaskResponseDTO();
        task1.setId(10L);
        task1.setTitle("Task 1");
        TaskResponseDTO task2 = new TaskResponseDTO();
        task2.setId(11L);
        task2.setTitle("Task 2");
        List<TaskResponseDTO> tasks = Arrays.asList(task1, task2);
        dto.setTasks(tasks);
        assertEquals(2, dto.getTasks().size());
        assertEquals(10L, dto.getTasks().get(0).getId());
        assertEquals("Task 2", dto.getTasks().get(1).getTitle());
    }
}

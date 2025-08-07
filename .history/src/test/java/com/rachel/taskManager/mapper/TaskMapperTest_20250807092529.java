package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    @Test
    void testToEntity() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");
        dto.setStatus("todo");
        dto.setProjectId(42L);

        Task task = TaskMapper.toEntity(dto);
        assertNull(task.getId()); // id should not be set by the mapper
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals("todo", task.getStatus());
    }

    @Test
    void testToDTO() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setStatus("done");

        TaskResponseDTO dto = TaskMapper.toDTO(task);
        assertEquals(1L, dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Desc", dto.getDescription());
        assertEquals("done", dto.getStatus());
    }
}

package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.ProjectSummaryDTO;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.Task;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMapperTest {
    private final ProjectMapper mapper = new ProjectMapper();

    @Test
    void testToEntity() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Project1");
        dto.setDescription("Desc1");
        Project project = ProjectMapper.toEntity(dto);
        assertEquals("Project1", project.getName());
        assertEquals("Desc1", project.getDescription());
    }

    @Test
    void testToEntityList() {
        ProjectRequestDTO dto1 = new ProjectRequestDTO();
        dto1.setName("A");
        dto1.setDescription("B");
        ProjectRequestDTO dto2 = new ProjectRequestDTO();
        dto2.setName("C");
        dto2.setDescription("D");
        List<Project> projects = ProjectMapper.toEntityList(Arrays.asList(dto1, dto2));
        assertEquals(2, projects.size());
        assertEquals("A", projects.get(0).getName());
        assertEquals("D", projects.get(1).getDescription());
    }

    @Test
    void testToEntityListNull() {
        assertNull(ProjectMapper.toEntityList(null));
    }

    @Test
    void testToDTO() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Proj");
        project.setDescription("Desc");
        Task task = new Task();
        task.setId(2L);
        task.setTitle("Task1");
        java.util.Set<Task> taskSet = java.util.Collections.singleton(task);
        project.setTasks(taskSet);
        ProjectResponseDTO dto = mapper.toDTO(project);
        assertEquals(1L, dto.getId());
        assertEquals("Proj", dto.getName());
        assertEquals("Desc", dto.getDescription());
        assertNotNull(dto.getTasks());
        assertEquals(1, dto.getTasks().size());
        assertEquals(2L, dto.getTasks().get(0).getId());
    }

    @Test
    void testToDTOWithNullTasks() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Proj");
        project.setDescription("Desc");
        project.setTasks(null);
        ProjectResponseDTO dto = mapper.toDTO(project);
        assertNotNull(dto);
        assertNull(dto.getTasks());
    }

    @Test
    void testToSummaryDTO() {
        Project project = new Project();
        project.setId(5L);
        project.setName("Summary");
        project.setDescription("SumDesc");
        ProjectSummaryDTO dto = mapper.toSummaryDTO(project);
        assertEquals(5L, dto.getId());
        assertEquals("Summary", dto.getName());
        assertEquals("SumDesc", dto.getDescription());
    }
}

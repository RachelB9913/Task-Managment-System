package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private Project project;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setCognitoSub("abc123");
        user.setMail("user@example.com");

        project = new Project();
        project.setId(1L);
        project.setUser(user);
        project.setTasks(new HashSet<>());

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus("todo");
        task.setUser(user);
        task.setProject(project);
    }

    @Test
    public void testCreateTask_Success() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Test Task");
        dto.setDescription("Description");
        dto.setStatus("todo");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.createTask(1L, dto, user);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testCreateTask_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        TaskRequestDTO dto = new TaskRequestDTO();
        assertThrows(RuntimeException.class, () -> {
            taskService.createTask(1L, dto, user);
        });
    }

    @Test
    public void testCreateTask_UnauthorizedUser() {
        User other = new User();
        other.setCognitoSub("wrong");
        other.setMail("intruder@example.com");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        TaskRequestDTO dto = new TaskRequestDTO();
        assertThrows(SecurityException.class, () -> {
            taskService.createTask(1L, dto, other);
        });
    }

    @Test
    public void testGetTasksByProjectIdAndUser_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findAllByProjectIdAndUser(1L, user, pageable)).thenReturn(page);

        Page<TaskResponseDTO> result = taskService.getTasksByProjectIdAndUser(1L, user, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testGetTasksByProjectIdAndUser_Unauthorized() {
        User hacker = new User();
        hacker.setCognitoSub("xxx");
        hacker.setMail("hack@example.com");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Pageable pageable = PageRequest.of(0, 5);

        assertThrows(SecurityException.class, () -> {
            taskService.getTasksByProjectIdAndUser(1L, hacker, pageable);
        });
    }

    @Test
    public void testGetTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO result = taskService.getTaskById(1L, user);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    public void testGetTaskById_Unauthorized() {
        User hacker = new User();
        hacker.setCognitoSub("unauthorized");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(SecurityException.class, () -> {
            taskService.getTaskById(1L, hacker);
        });
    }

    @Test
    public void testUpdateTask_Success() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Updated");
        dto.setDescription("Updated desc");
        dto.setStatus("done");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.updateTask(1L, dto, user);

        assertNotNull(result);
        assertEquals("Updated", task.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    public void testUpdateTask_Unauthorized() {
        User hacker = new User();
        hacker.setCognitoSub("unauthorized");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskRequestDTO dto = new TaskRequestDTO();
        assertThrows(SecurityException.class, () -> {
            taskService.updateTask(1L, dto, hacker);
        });
    }

    @Test
    public void testDeleteTask_Success() {
        project.getTasks().add(task);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, user);

        verify(taskRepository).delete(task);
        assertFalse(project.getTasks().contains(task));
    }

    @Test
    public void testDeleteTask_Unauthorized() {
        User hacker = new User();
        hacker.setCognitoSub("unauthorized");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(SecurityException.class, () -> {
            taskService.deleteTask(1L, hacker);
        });
    }
}

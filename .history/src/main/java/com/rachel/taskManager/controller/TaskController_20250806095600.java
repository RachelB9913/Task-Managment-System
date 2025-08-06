package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.mapper.TaskMapper;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.TaskService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks") // Tasks belong to a project
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;

    // Get a specific task by ID (no need for projectId here, just taskId)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // Get all tasks for a specific project
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@PathVariable Long projectId, @CurrentUser User currentUser) {
        List<Task> tasks = taskService.getTasks(projectId, currentUser);
        return tasks.isEmpty() 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.ok(tasks.stream().map(mapper::toDTO).toList());
    }

    // Create a task for a specific project
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskService.createTask(projectId, taskRequestDTO));
    }

    // Update a task
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequestDTO));
    }

    // Delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}

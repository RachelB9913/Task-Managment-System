package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
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

    // Get a specific task by ID (no need for projectId here, just taskId)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long taskId,
            @CurrentUser User currentUser) {
        // Optionally, you can add user-based access checks in the service
        return ResponseEntity.ok(taskService.getTaskById(taskId, currentUser));
    }

    // Get all tasks for a specific project
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(
            @PathVariable Long projectId,
            @CurrentUser User currentUser) {
        List<TaskResponseDTO> tasks = taskService.getTasksByProjectIdAndUser(projectId, currentUser);
        return tasks.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(tasks);
    }

    // Create a task for a specific project
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequestDTO taskRequestDTO,
            @CurrentUser User currentUser) {
        return ResponseEntity.ok(taskService.createTask(projectId, taskRequestDTO, currentUser));
    }

    // Update a task
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequestDTO,
            @CurrentUser User currentUser) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequestDTO, currentUser));
    }

    // Delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @CurrentUser User currentUser) {
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
}

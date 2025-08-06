package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.TaskService;
import lombok.RequiredArgsConstructor;
import static com.rachel.taskManager.util.LogUtils.formatUser;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks") // Tasks belong to a project
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    // Get a specific task by ID (no need for projectId here, just taskId)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long taskId,
            @CurrentUser User currentUser) {
        logger.info("User [{}] requested to get task {}", formatUser(currentUser), taskId);
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
        logger.info("User [{}] asked to create a task for project {}", formatUser(currentUser), projectId);
        return ResponseEntity.ok(taskService.createTask(projectId, taskRequestDTO, currentUser));
    }

    // Update a task
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequestDTO,
            @CurrentUser User currentUser) {
        logger.info("User [{}] asked to update task {}", formatUser(currentUser), taskId);
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

package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.TaskService;

import lombok.RequiredArgsConstructor;
import static com.rachel.taskManager.util.LogUtils.formatUser;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks") // Tasks belong to a project
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


    @PreAuthorize("hasRole('ADMIN') or #currentUser.cognitoSub == authentication.name")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long taskId,
            @CurrentUser User currentUser) {
        logger.info("User [{}] requested to get task {}", formatUser(currentUser), taskId);
        return ResponseEntity.ok(taskService.getTaskById(taskId, currentUser));
    }


    @PreAuthorize("hasRole('ADMIN') or #currentUser.cognitoSub == authentication.name")
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(
            @PathVariable Long projectId,
            @CurrentUser User currentUser,
            Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.getTasksByProjectIdAndUser(projectId, currentUser, pageable);
        return ResponseEntity.ok(tasks);
    }


    @PreAuthorize("hasRole('ADMIN') or #currentUser.cognitoSub == authentication.name")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequestDTO taskRequestDTO,
            @CurrentUser User currentUser) {
        logger.info("User [{}] requested to create a task for project {}", formatUser(currentUser), projectId);
        return ResponseEntity.ok(taskService.createTask(projectId, taskRequestDTO, currentUser));
    }


    @PreAuthorize("hasRole('ADMIN') or #currentUser.cognitoSub == authentication.name")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequestDTO,
            @CurrentUser User currentUser) {
        logger.info("User [{}] requested to update task {}", formatUser(currentUser), taskId);
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequestDTO, currentUser));
    }


    @PreAuthorize("hasRole('ADMIN') or #currentUser.cognitoSub == authentication.name")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @CurrentUser User currentUser) {
        logger.info("User [{}] requested to delete task {}", formatUser(currentUser), taskId);
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
}

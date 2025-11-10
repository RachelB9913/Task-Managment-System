package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.TaskService;
import com.rachel.taskManager.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    // --- TASKS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tasks/all")
    public ResponseEntity<PaginatedResponse<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDTO> page = taskService.getAllTasks(pageable);
        return ResponseEntity.ok(PaginatedResponse.from(page));
    }

    // --- PROJECTS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects/all")
    public ResponseEntity<PaginatedResponse<ProjectResponseDTO>> getAllProjects(Pageable pageable) {
        Page<ProjectResponseDTO> page = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(PaginatedResponse.from(page));
    }

    // --- USERS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public ResponseEntity<PaginatedResponse<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> page = userService.getAllUsers(pageable);
        return ResponseEntity.ok(PaginatedResponse.from(page));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{sub}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String sub) {
        try {
            UserDTO user = userService.getCurrentUser(sub);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{sub}")
    public ResponseEntity<Void> deleteUser(@PathVariable String sub) {
        userService.deleteUser(sub);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{sub}/role/{newRole}") // new role maybe 'USER' or 'ADMIN'
    public ResponseEntity<String> updateUserRole(@PathVariable String sub, @PathVariable String newRole) {
        userService.updateUserRole(sub, newRole);
        return ResponseEntity.ok(String.format("User %s role changed to %s successfully.", sub, newRole));
    }
}

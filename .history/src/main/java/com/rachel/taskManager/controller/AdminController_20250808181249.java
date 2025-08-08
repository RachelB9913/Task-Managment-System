package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.repository.UserRepository;
import com.rachel.taskManager.dto.UpdateRoleRequest;
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
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final UserRepository userRepository;

    // --- TASKS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tasks/all")
    public ResponseEntity<PaginatedResponse<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDTO> page = taskService.getAllTasks(pageable);
        PaginatedResponse<TaskResponseDTO> response = new PaginatedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
        return ResponseEntity.ok(response);
    }

    // --- PROJECTS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects/all")
    public ResponseEntity<PaginatedResponse<ProjectResponseDTO>> getAllProjects(Pageable pageable) {
        Page<ProjectResponseDTO> page = projectService.getAllProjects(pageable);
        PaginatedResponse<ProjectResponseDTO> response = new PaginatedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
        return ResponseEntity.ok(response);
    }

    // --- USERS ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
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

    @PreAuthorize("hasRole('ADMIN')")  //TODO -understand
    @PutMapping("/users/{sub}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable String sub, @RequestBody UpdateRoleRequest request) {
        userService.updateUserRole(sub, request.getNewRole());
        return ResponseEntity.noContent().build();
    }
}

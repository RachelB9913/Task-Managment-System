package com.rachel.taskManager.controller;

import lombok.RequiredArgsConstructor;

import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.ProjectService;

import static com.rachel.taskManager.util.LogUtils.formatUser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    // A controller to manage project-related endpoints, including creating, retrieving, updating, and deleting projects.

    private final ProjectService projectService;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
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


    @PreAuthorize("hasRole('ADMIN') or #user.cognitoSub == authentication.name")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable Long projectId, @CurrentUser User user) {
        return ResponseEntity.ok(projectService.getProjectById(projectId, user));
    }


    @PreAuthorize("hasRole('ADMIN') or #user.cognitoSub == authentication.name")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectDTO,
                                                            @CurrentUser User user) {
        logger.info("User [{}] requested to create a project", formatUser(user));
        ProjectResponseDTO created = projectService.createProject(projectDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PreAuthorize("hasRole('ADMIN') or #user.cognitoSub == authentication.name")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId,
                                                            @RequestBody ProjectRequestDTO dto,
                                                            @CurrentUser User user) {
        logger.info("User [{}] requested to update project {}", formatUser(user), projectId);
        return ResponseEntity.ok(projectService.updateProject(projectId, dto, user));
    }

    @PreAuthorize("hasRole('ADMIN') or #user.cognitoSub == authentication.name")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, @CurrentUser User user) {
        projectService.deleteProject(projectId, user);
        return ResponseEntity.noContent().build();
    }
}


package com.rachel.taskManager.controller;

import lombok.RequiredArgsConstructor;

import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.UserService;
import static com.rachel.taskManager.util.LogUtils.formatUser;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable Long projectId, @CurrentUser User user) {
        return ResponseEntity.ok(projectService.getProjectById(projectId, user));
    }

    @GetMapping("/my-projects")
    public List<ProjectResponseDTO> getProjectsForUser(@CurrentUser User user) {
        logger.info("Fetching projects for user: {}", formatUser(user));
        return userService.getProjectsForUser(user.getCognitoSub());
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectDTO,
                                                            @CurrentUser User user) {
        logger.info("User [{}] requested to create a project", formatUser(user));
        ProjectResponseDTO created = projectService.createProject(projectDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId,
                                                            @RequestBody ProjectRequestDTO dto,
                                                            @CurrentUser User user) {
        logger.info("User [{}] requested to update project {}", formatUser(user), projectId);
        return ResponseEntity.ok(projectService.updateProject(projectId, dto, user));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}


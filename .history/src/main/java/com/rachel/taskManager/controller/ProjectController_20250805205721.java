package com.rachel.taskManager.controller;

import lombok.RequiredArgsConstructor;

import com.rachel.taskManager.dto.ProjectDTO;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.UserService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/my-projects")
    public List<ProjectResponseDTO> getProjectsForUser(@CurrentUser User user) {
        return userService.getProjectsForUser(user.getCognitoSub());
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectRequestDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

     @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId,
                                                            @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(projectId, dto));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}


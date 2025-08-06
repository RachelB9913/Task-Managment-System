package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.ProjectSummaryDTO;
import com.rachel.taskManager.dto.ProjectWithTaskSummaryDTO;
import com.rachel.taskManager.dto.TaskSummaryDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.UserRepository;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDTO getCurrentUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<ProjectResponseDTO> getProjectsForUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getProjects().stream().map(projectMapper::toDTO).toList();
    }

    public User getOrCreateUserFromToken(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");

        if (sub == null) {
            logger.error("JWT token does not contain 'sub' claim");
            throw new IllegalArgumentException("Invalid JWT token: 'sub' claim is missing");
        }
        
        // Try to find the user with full graph
        return userRepository.findByCognitoSub(sub)
            .orElseGet(() -> {
                User user = userMapper.fromJwt(jwt);
                userRepository.save(user);
                return userRepository.findByCognitoSub(sub)
                        .orElseThrow(() -> new IllegalStateException("User not found after save"));
            });
    }

    public UserDTO getCurrentUserSummary(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<ProjectSummaryDTO> projectSummaries = user.getProjects().stream()
            .map(project -> {
                ProjectSummaryDTO summary = new ProjectSummaryDTO();
                summary.setId(project.getId());
                summary.setName(project.getName());
                summary.setDescription(project.getDescription());
                return summary;
            })
            .toList();

        UserDTO dto = new UserDTO();
        dto.setMail(user.getMail());
        dto.setAdmin(user.isAdmin());
        dto.setProjects(projectSummaries);

        return dto;
    }

    public Page<ProjectWithTaskSummaryDTO> getProjectsWithTasks(String sub, Pageable pageable) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Page<Project> projectsPage = projectRepository.findByUser(user, pageable);

        return projectsPage.map(project -> {
            ProjectWithTaskSummaryDTO dto = new ProjectWithTaskSummaryDTO();
            dto.setId(project.getId());
            dto.setName(project.getName());
            dto.setDescription(project.getDescription());

            List<TaskSummaryDTO> taskSummaries = project.getTasks().stream()
                .map(task -> {
                    TaskSummaryDTO t = new TaskSummaryDTO();
                    t.setTitle(task.getTitle());
                    t.setStatus(task.getStatus());
                    return t;
                }).toList();

            dto.setTasks(taskSummaries);
            return dto;
        });
    }
}
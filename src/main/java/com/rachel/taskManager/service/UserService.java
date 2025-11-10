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
import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import com.rachel.taskManager.config.CognitoProperties;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRemoveUserFromGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final CognitoIdentityProviderClient cognitoClient;
    private final CognitoProperties cognitoProperties;


    public UserDTO getCurrentUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }


    public List<ProjectResponseDTO> getProjectsForUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getProjects().stream()
            .map(project -> projectMapper.toDTO(project))
            .toList();
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


    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toDTO);
    }


     public void deleteUserFromCognito(String username) {
    // use injected client
    var deleteUserRequest = AdminDeleteUserRequest.builder()
        .userPoolId(cognitoProperties.getUserPoolId())
        .username(username)
        .build();
    cognitoClient.adminDeleteUser(deleteUserRequest);
    }


    public void deleteUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Delete from Cognito
        deleteUserFromCognito(user.getCognitoSub());
        // Delete from local DB
        userRepository.deleteById(user.getCognitoSub());
    }


    public void updateUserRole(String sub, String newRole) {
        String normalized = newRole == null ? "" : newRole.trim().toUpperCase();
        if (!normalized.equals("ADMIN") && !normalized.equals("USER")) {
            throw new IllegalArgumentException("newRole must be ADMIN or USER");
        }

        User user = userRepository.findByCognitoSub(sub)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setAdmin("ADMIN".equals(normalized));
        userRepository.save(user);

        updateUserRoleInCognito(user.getCognitoSub(), normalized);
    }

    public void updateUserRoleInCognito(String sub, String newRole) {
    // update custom attribute
    var updateReq = AdminUpdateUserAttributesRequest.builder()
        .userPoolId(cognitoProperties.getUserPoolId())
        .username(sub)
        .userAttributes(AttributeType.builder()
            .name("custom:role")
            .value(newRole)
            .build())
        .build();
    cognitoClient.adminUpdateUserAttributes(updateReq);

    // manage group membership
    if ("ADMIN".equalsIgnoreCase(newRole)) {
        var addReq = AdminAddUserToGroupRequest.builder()
            .userPoolId(cognitoProperties.getUserPoolId())
            .username(sub)
            .groupName("ADMIN")
            .build();
        cognitoClient.adminAddUserToGroup(addReq);
    } else { // treat anything else as USER - remove from ADMIN
        var removeReq = AdminRemoveUserFromGroupRequest.builder()
            .userPoolId(cognitoProperties.getUserPoolId())
            .username(sub)
            .groupName("ADMIN")
            .build();
        cognitoClient.adminRemoveUserFromGroup(removeReq);
    }
    }
}

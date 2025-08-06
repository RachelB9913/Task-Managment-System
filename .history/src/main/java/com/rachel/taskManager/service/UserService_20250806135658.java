package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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

        return user.getProjects()
            .stream()
            .map(projectMapper::toResponseDTO)
            .toList();
    }

    public User getOrCreateUserFromToken(Jwt jwt) {
        logger.info("Attempting to get or create user from JWT token");

        String sub = jwt.getClaimAsString("sub");

        if (sub == null) {
            logger.error("JWT token does not contain 'sub' claim");
            throw new IllegalArgumentException("Invalid JWT token: 'sub' claim is missing");
        }
        
        // Try to find the user with full graph
        return userRepository.findByCognitoSubWithProjectsAndTasks(sub)
            .orElseGet(() -> {
                User user = userMapper.fromJwt(jwt);
                userRepository.save(user);

                // Important: re-fetch using EntityGraph to load projects+tasks
                return userRepository.findByCognitoSubWithProjectsAndTasks(sub)
                        .orElseThrow(() -> new IllegalStateException("Failed to re-fetch user after saving"));
            });
    }
}
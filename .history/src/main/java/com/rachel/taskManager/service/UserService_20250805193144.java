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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    public UserDTO getCurrentUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<ProjectResponseDTO> getProjectsForUser(String sub) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getProjects()
            .stream()
            .map(projectMapper::toResponseDTO)
            .toList();
    }

    public User getOrCreateUserFromToken(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");
        return userRepository.findByCognitoSub(sub)
            .orElseGet(() -> {
                User user = userMapper.fromJwt(jwt);
                return userRepository.save(user);
            });
    }
}
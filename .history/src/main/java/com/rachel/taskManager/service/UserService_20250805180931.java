package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.ProjectDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

     private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getCurrentUser(String sub) {
        User user = userRepository.findByCognitoSub(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<ProjectResponseDTO> getProjectsForUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));

    return user.getProjects()
        .stream()
        .map(projectMapper::toResponseDTO)
        .toList();
}
}
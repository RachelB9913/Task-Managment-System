package com.rachel.taskManager.service;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<ProjectDTO> getProjectsForUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getProjects()
            .stream()
            .map(project -> userMapper.getProjectMapper().toDTO(project))
            .toList();
    }
}
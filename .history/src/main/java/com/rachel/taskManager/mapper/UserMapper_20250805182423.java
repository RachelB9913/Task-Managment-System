package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ProjectMapper projectMapper;

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setMail(user.getMail());
        dto.setAdmin(user.isAdmin());
        dto.setProjects(null != user.getProjects() ? 
            user.getProjects().stream()
                .map(projectMapper::toRequestDTO)
                .toList() : null);
        return dto;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setMail(userDTO.getMail());
        user.setAdmin(userDTO.isAdmin());
        return user;
    }
}

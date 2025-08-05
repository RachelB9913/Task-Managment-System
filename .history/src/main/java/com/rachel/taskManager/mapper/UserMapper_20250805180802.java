package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;

@Component
public class UserMapper {

    private final ProjectMapper projectMapper;

    public UserMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setMail(user.getMail());
        dto.setIsAdmin(user.isAdmin());
        dto.setProjects(user.getProjects()
            .stream()
            .map(projectMapper::toResponseDTO)
            .toList());
        return dto;
    }
}



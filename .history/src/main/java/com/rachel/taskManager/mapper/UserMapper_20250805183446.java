package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.mapper.ProjectMapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ProjectMapper projectMapper;

    public ProjectResponseDTO toResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        return dto;
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setMail(user.getMail());
        dto.setAdmin(user.isAdmin());
        
        // Now that projects is a List<Project>, just stream it directly
        dto.setProjects(user.getProjects() != null ?
            user.getProjects().stream()
                .map(projectMapper::toResponseDTO)
                .toList()
            : null
        );

        return dto;
    }
    
}

package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;


@Component
@RequiredArgsConstructor
public class UserMapper {
    // A mapper class to convert between UserDTO and User entities.

    private final ProjectMapper projectMapper;

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setMail(user.getMail());
        dto.setAdmin(user.isAdmin());

        dto.setProjects(user.getProjects() != null ?
            user.getProjects().stream()
                .map(projectMapper::toSummaryDTO)
                .toList()
            : null
        );

        return dto;
    }

    public User fromJwt(Jwt jwt) {
        User user = new User();
        user.setCognitoSub(jwt.getClaimAsString("sub"));
        user.setMail(jwt.getClaimAsString("email"));
        user.setAdmin(jwt.getClaimAsBoolean("admin") != null && jwt.getClaimAsBoolean("admin"));
        return user;
    }
}

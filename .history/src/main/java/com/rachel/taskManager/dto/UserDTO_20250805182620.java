package com.rachel.taskManager.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDTO {
    private String mail;
    private boolean isAdmin;
    private List<ProjectResponseDTO> projects;
}

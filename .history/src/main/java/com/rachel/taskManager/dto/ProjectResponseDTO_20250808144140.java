package com.rachel.taskManager.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private List<TaskResponseDTO> tasks; // List of tasks associated with this project

}

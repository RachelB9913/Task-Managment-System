package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class ProjectRequestDTO {
    
    // can add @NotBlank, @Size, etc. for validation
    private String name;
    private String description;
    
}
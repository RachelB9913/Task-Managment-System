package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status; // "todo", "in_progress", "done"
        
}

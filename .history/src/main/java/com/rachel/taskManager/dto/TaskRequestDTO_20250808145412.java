package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class TaskRequestDTO {
    
    private long projectId; // The ID of the project this task belongs to
    private String title;
    private String description;
    private String status; // "todo", "in_progress", "done" or any other custom status

}

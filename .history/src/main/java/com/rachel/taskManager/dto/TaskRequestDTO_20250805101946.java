package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class TaskRequestDTO {

    private String title;
    private String description;
    private String status; // "todo", "in_progress", "done"
    private Long projectId; // ID of the project this task belongs to

}

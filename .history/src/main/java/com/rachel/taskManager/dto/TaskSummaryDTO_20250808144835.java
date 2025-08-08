package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class TaskSummaryDTO {
    // A DTO to use as a shorter version of TaskResponseDTO, typically used in lists.
    
    private String title;
    private String status;

}

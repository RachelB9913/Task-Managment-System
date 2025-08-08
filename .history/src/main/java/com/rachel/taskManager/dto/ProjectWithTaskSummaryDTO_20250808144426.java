package com.rachel.taskManager.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectWithTaskSummaryDTO {

    private Long id;
    private String name;
    private String description;
    private List<TaskSummaryDTO> tasks;
    
}

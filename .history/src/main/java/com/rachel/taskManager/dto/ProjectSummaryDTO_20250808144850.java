package com.rachel.taskManager.dto;

import lombok.Data;

@Data
public class ProjectSummaryDTO {
    // A DTO to use as a shorter version of ProjectWithTaskSummaryDTO, typically used in lists.

    private Long id;
    private String name;
    private String description;

}

package com.rachel.taskManager.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status; // "todo", "in_progress", "done"

    private List<TaskSummaryDTO> subTasks = Collections.unmodifiableList(new ArrayList<>());
    
    public List<TaskSummaryDTO> getSubTasks() {
        return subTasks;
    }
        
}

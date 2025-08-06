package com.rachel.taskManager.dto;

import com.rachel.taskManager.model.Status;
import lombok.Data;

@Data
public class TaskSummaryDTO {
    private String title;
    private Status status;
}

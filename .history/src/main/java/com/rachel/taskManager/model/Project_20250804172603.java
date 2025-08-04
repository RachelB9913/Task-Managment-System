package com.rachel.taskManager.model;

import java.util.HashMap;
import java.util.Map;

public class Project {
    
    private Long id;
    private String name;
    private String description;
    private Map<Long, Task> tasks; // Assuming tasks are stored in a Map with task ID as key

    public Project(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = new HashMap<>(); // Initialize tasks as empty
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<Long, Task> getTasks() { return tasks; }
    public void setTasks(Map<Long, Task> tasks) { this.tasks = tasks; }
}

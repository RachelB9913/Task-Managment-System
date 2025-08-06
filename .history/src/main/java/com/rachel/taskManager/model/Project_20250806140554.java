package com.rachel.taskManager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;


@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Task> tasks; // Assuming tasks are stored in a Map with task ID as key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Assuming each task is associated with a user //TODO - maybe more thanone user?

    public Project() {
        this.tasks = new HashSet<>();
    }
    
    public Project(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = new HashSet<>();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } //TODO - maybe do not allow setting ID after creation

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Task> getTasks() { return tasks; }
    public void setTasks(Set<Task> tasks) { this.tasks = tasks; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; } // TODO - maybe to set by userId
}

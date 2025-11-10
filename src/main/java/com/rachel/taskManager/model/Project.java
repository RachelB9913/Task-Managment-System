package com.rachel.taskManager.model;

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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Task> tasks; // Set of tasks associated with this project

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Project() {
        this.tasks = new HashSet<>();
    }
    
    public Project(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = new HashSet<>();
    }

    // equals and hashCode based on id - because using set and not list
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return id != null ? id.equals(project.id) : project.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Task> getTasks() { return tasks; }
    public void setTasks(Set<Task> tasks) { this.tasks = tasks; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
}

package com.rachel.taskManager.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String mail;
    private String password;
    private boolean isAdmin;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Long, Project> projects; // Assuming projects are stored in a Map with project ID as key

    public User(Long id, String username, String mail, String password) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.isAdmin = false; // Default to false
        this.projects = new HashMap<>(); // Initialize projects as empty
    }

    public User(Long id, String username, String mail, String password, boolean isAdmin, Map<Long, Project> projects) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.isAdmin = isAdmin;
        this.projects = projects;
    }


    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public boolean isAdmin() {return isAdmin;}
    public void setAdmin(boolean isAdmin) {this.isAdmin = isAdmin;}

    public Map<Long, Project> getProjects() {return projects;}
    public void setProjects(Map<Long, Project> projects) {this.projects = projects;}
}

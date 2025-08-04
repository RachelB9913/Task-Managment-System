package com.rachel.taskManager.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    
    private Long id;
    private String username;
    private String mail;
    private String password;
    private boolean isAdmin;
    private Map<Long, Project> projects; // Assuming projects are stored in a Map with project ID as key

    public User(Long id, String username, String mail, String password) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.isAdmin = false; // Default to false, can be set later
        this.projects = new HashMap<>(); // Initialize projects as null or empty if needed
    }

    public User(Long id, String username, String mail, String password, boolean isAdmin, Map<Long, Project> projects) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.isAdmin = isAdmin;
        this.projects = projects;
    }
}

package com.rachel.taskManager.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
    
    @Id
    private String cognitoSub; // Store Cognito's unique user ID ("sub" from id_token)

    private String mail;
    private boolean isAdmin;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();
    
    public User() {}

    public User(String cognitoSub, String mail) {
        this.cognitoSub = cognitoSub;
        this.mail = mail;
        this.isAdmin = false;
    }

    // Getters and Setters
    public String getCognitoSub() {return cognitoSub;}
    public void setCognitoSub(String cognitoSub) {this.cognitoSub = cognitoSub;}

    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}

    public boolean isAdmin() {return isAdmin;}
    public void setAdmin(boolean isAdmin) {this.isAdmin = isAdmin;}

    public Set<Project> getProjects() {return projects;}
    public void setProjects(Set<Project> projects) {this.projects = projects;}
}

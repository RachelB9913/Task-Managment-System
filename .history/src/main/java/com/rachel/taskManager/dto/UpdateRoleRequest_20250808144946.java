package com.rachel.taskManager.dto;

public class UpdateRoleRequest {
    // A DTO to represent a request to update a user's role in the system.
    
    private String newRole;

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}

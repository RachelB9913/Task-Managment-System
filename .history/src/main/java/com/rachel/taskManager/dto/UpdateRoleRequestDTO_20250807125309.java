package com.rachel.taskManager.dto;

public class UpdateRoleRequestDTO {
    private String newRole;

    public UpdateRoleRequestDTO() {
    }

    public UpdateRoleRequestDTO(String newRole) {
        this.newRole = newRole;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}

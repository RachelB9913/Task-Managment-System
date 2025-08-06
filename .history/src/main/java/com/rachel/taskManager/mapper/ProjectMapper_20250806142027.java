package com.rachel.taskManager.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.Project;

@Component
public class ProjectMapper {
    public static Project toEntity(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        return project;
    }

    public static ProjectResponseDTO toDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        if (project.getTasks() != null) {
            dto.setTasks(project.getTasks().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    public static List<Project> toEntityList(List<ProjectRequestDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
            .map(ProjectMapper::toEntity)
            .collect(Collectors.toList());
    }

    public ProjectResponseDTO toResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        if (project.getTasks() != null) {
            dto.setTasks(project.getTasks().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

}

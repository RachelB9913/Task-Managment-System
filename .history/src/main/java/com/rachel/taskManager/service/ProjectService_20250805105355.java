package com.rachel.taskManager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.mapper.ProjectMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        return ProjectMapper.toDTO(project);
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO) {
        Project project = ProjectMapper.toEntity(projectDTO);
        Project saved = projectRepository.save(project);
        return ProjectMapper.toDTO(saved);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project existing = getProjectById(id);
        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());
        return projectRepository.save(existing);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}

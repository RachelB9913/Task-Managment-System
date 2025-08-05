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

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        Project updated = projectRepository.save(existing);
        return ProjectMapper.toDTO(updated);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        projectRepository.delete(project);
    }

}

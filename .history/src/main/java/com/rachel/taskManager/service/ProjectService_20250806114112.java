package com.rachel.taskManager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.mapper.ProjectMapper;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    
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

    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO, User user) {
        logger.info("Current user is creating a project");
        Project project = ProjectMapper.toEntity(projectDTO);
        project.setUser(user);
        Project saved = projectRepository.save(project);
        logger.info("Project {} created successfully by current user", saved.getId());
        return ProjectMapper.toDTO(saved);
    }


    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
        logger.info("Current user is updating project with id {}", id);
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        Project updated = projectRepository.save(existing);
        logger.info("Project {} updated successfully by current user", updated.getId());
        return ProjectMapper.toDTO(updated);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        projectRepository.delete(project);
        logger.info("Project {} deleted successfully", id);
    }

}

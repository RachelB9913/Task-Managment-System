package com.rachel.taskManager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.mapper.ProjectMapper;
import static com.rachel.taskManager.util.LogUtils.formatUser;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private void checkProjectOwnership(Project project, User currentUser) {
        if (!project.getUser().equals(currentUser)) {
            logger.error("User {} attempted to access project {} without permission", currentUser.getMail(), project.getId());
            throw new SecurityException("Access denied");
        }
    }
    
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(Long id, User currentUser) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        checkProjectOwnership(project, currentUser);
        logger.info("User [{}] accessed project {}", formatUser(currentUser), id);
        return ProjectMapper.toDTO(project);
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO, User user) {
        logger.info("Current user is creating a project");
        Project project = ProjectMapper.toEntity(projectDTO);
        project.setUser(user);
        Project saved = projectRepository.save(project);
        logger.info("Project {} created successfully by {}", saved.getId(), formatUser(user));
        return ProjectMapper.toDTO(saved);
    }


    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto, User user) {
        logger.info("Current user is updating project with id {}", id);
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        
        checkProjectOwnership(existing, user);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        Project updated = projectRepository.save(existing);
        logger.info("Project {} updated successfully by {}", updated.getId(), formatUser(updated.getUser()));
        return ProjectMapper.toDTO(updated);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        projectRepository.delete(project);
        logger.info("Project {} deleted successfully", id);
    }

}

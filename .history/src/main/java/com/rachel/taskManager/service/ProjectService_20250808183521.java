package com.rachel.taskManager.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.mapper.ProjectMapper;
import static com.rachel.taskManager.util.LogUtils.formatUser;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private void checkProjectOwnershipOrAdmin(Project project, User currentUser) {
        boolean isAdmin = currentUser.isAdmin();
        if (!isAdmin && !project.getUser().equals(currentUser)) {
            logger.error("User [{}] attempted to access project {} without permission", currentUser.getMail(), project.getId());
            throw new SecurityException("Access denied");
        }
    }

    
    public Page<ProjectResponseDTO> getProjectsByUser(User user, Pageable pageable) {
        logger.info("User [{}] requested their projects", formatUser(user));
        Page<Project> projects = projectRepository.findByUser(user, pageable);
        if (projects.isEmpty()) {
            throw new NoSuchElementException("No projects found for user");
        }
        return projects.map(projectMapper::toDTO);
    }
    

    public Page<ProjectResponseDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toDTO);
    }


    public ProjectResponseDTO getProjectById(Long id, User currentUser) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        checkProjectOwnershipOrAdmin(project, currentUser);
        logger.info("User [{}] accessed project {}", formatUser(currentUser), id);
        return projectMapper.toDTO(project);
    }


    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO, User user) {
        logger.info("Current user is creating a project");
        Project project = ProjectMapper.toEntity(projectDTO);
        project.setUser(user);
        Project saved = projectRepository.save(project);
        logger.info("Project {} created successfully by [{}]", saved.getId(), formatUser(user));
        return projectMapper.toDTO(saved);
    }


    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto, User user) {
        logger.info("[{}] is updating project with id {}", formatUser(user), id);
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        checkProjectOwnershipOrAdmin(existing, user);
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        Project updated = projectRepository.save(existing);
        logger.info("Project {} updated successfully by [{}]", updated.getId(), formatUser(updated.getUser()));
        return projectMapper.toDTO(updated);
    }


    public void deleteProject(Long id, User user) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        checkProjectOwnershipOrAdmin(project, user);
        projectRepository.delete(project);
        logger.info("Project {} deleted successfully by [{}]", id, formatUser(user));
        if (projectRepository.existsById(id)) {
            logger.error("Project {} was not deleted from the database!", id);
            throw new IllegalStateException("Project was not deleted");
        }
    }

}

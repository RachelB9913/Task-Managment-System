    // Get all tasks for all users (admin only)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskMapper::toDTO);
    }
package com.rachel.taskManager.service;

import java.util.NoSuchElementException;

import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.mapper.TaskMapper;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.TaskRepository;
import static com.rachel.taskManager.util.LogUtils.formatUser;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private void checkTaskOwnershipOrAdmin(Task task, User currentUser) {
        boolean isAdmin = currentUser.isAdmin();
        if (!isAdmin && !task.getUser().equals(currentUser)) {
            logger.error("User {} attempted to access task {} without permission", currentUser.getMail(), task.getId());
            throw new SecurityException("Access denied");
        }
    }
    

    public Page<TaskResponseDTO> getTasksByProjectIdAndUser(Long projectId, User currentUser, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        boolean isAdmin = currentUser.isAdmin();
        if (!isAdmin && !project.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }
        if (isAdmin) {
            return taskRepository.findAllByProjectId(projectId, pageable)
                .map(TaskMapper::toDTO);
        } else {
            return taskRepository.findAllByProjectIdAndUser(projectId, currentUser, pageable)
                .map(TaskMapper::toDTO);
        }
    }


    public TaskResponseDTO getTaskById(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnershipOrAdmin(task, currentUser);
        return TaskMapper.toDTO(task);
    }


    public TaskResponseDTO updateTask(Long id, TaskRequestDTO updatedTaskDTO, User currentUser) {
        logger.info("[{}] is updating task with id {}", formatUser(currentUser), id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnershipOrAdmin(task, currentUser);
        task.setTitle(updatedTaskDTO.getTitle());
        task.setDescription(updatedTaskDTO.getDescription());
        task.setStatus(updatedTaskDTO.getStatus());
        Task savedTask = taskRepository.save(task);
        logger.info("Task {} updated successfully by [{}]", savedTask.getId(), formatUser(savedTask.getUser()));
        return TaskMapper.toDTO(savedTask);
    }


    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnershipOrAdmin(task, currentUser);
        // Remove task from project's task set
        Project project = task.getProject();
        project.getTasks().remove(task);
        taskRepository.delete(task);
        logger.info("Task {} deleted successfully by [{}]", id, formatUser(currentUser));
    }

    
    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO taskDTO, User currentUser) {
        logger.info("[{}] is creating a task in project {}", formatUser(currentUser), projectId);
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        boolean isAdmin = currentUser.isAdmin();
        if (!isAdmin && !project.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }
        Task task = TaskMapper.toEntity(taskDTO);
        task.setProject(project);
        task.setUser(isAdmin ? project.getUser() : currentUser);
        Task savedTask = taskRepository.save(task);
        logger.info("Task {} created successfully by [{}]", savedTask.getId(), formatUser(currentUser));
        return TaskMapper.toDTO(savedTask);
    }
}
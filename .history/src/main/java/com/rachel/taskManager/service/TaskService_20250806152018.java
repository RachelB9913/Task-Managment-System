package com.rachel.taskManager.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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


@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private void checkTaskOwnership(Task task, User currentUser) {
        if (!task.getUser().equals(currentUser)) {
            logger.error("User {} attempted to access task {} without permission", currentUser.getMail(), task.getId());
            throw new SecurityException("Access denied");
        }
    }
    
    public List<TaskResponseDTO> getTasksByProjectIdAndUser(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        if (!project.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }

        return project.getTasks()
                .stream()
                .filter(task -> {
                    checkTaskOwnership(task, currentUser);
                    return true;
                })
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }


    public TaskResponseDTO getTaskById(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnership(task, currentUser);
        return TaskMapper.toDTO(task);
    }


    public TaskResponseDTO updateTask(Long id, TaskRequestDTO updatedTaskDTO, User currentUser) {
        logger.info("[{}] is updating task with id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        checkTaskOwnership(task, currentUser);
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
        checkTaskOwnership(task, currentUser);

        // Remove task from project's task set
        Project project = task.getProject();
        project.getTasks().remove(task);

        taskRepository.delete(task);
        logger.info("Task {} deleted successfully by current user", id);
    }

    
    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO taskDTO, User currentUser) {
        logger.info("Current user is creating a task in project {}", projectId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        if (!project.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }

        Task task = TaskMapper.toEntity(taskDTO);
        task.setProject(project);
        task.setUser(currentUser);
        Task savedTask = taskRepository.save(task);
        logger.info("Task {} created successfully by current user", savedTask.getId());
        return TaskMapper.toDTO(savedTask);
    }
}
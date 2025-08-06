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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private void checkTaskOwnership(Task task, User currentUser) {
        if (!task.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }
    }

    public List<TaskResponseDTO> getTasksByProjectIdAndUser(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        // TODO - check project ownership here if needed
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

    public List<Task> getTasks(Long projectId, User currentUser) {
    List<Task> tasks = taskRepository.findAllByProjectIdAndUser(projectId, currentUser);
    tasks.forEach(task -> checkTaskOwnership(task, currentUser));
    return tasks;
}

    public TaskResponseDTO getTaskById(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnership(task, currentUser);
        return TaskMapper.toDTO(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO updatedTaskDTO, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        checkTaskOwnership(task, currentUser);
        task.setTitle(updatedTaskDTO.getTitle());
        task.setDescription(updatedTaskDTO.getDescription());
        task.setStatus(updatedTaskDTO.getStatus());

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        checkTaskOwnership(task, currentUser);
        taskRepository.delete(task);
    }

    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO taskDTO, User currentUser) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        // TODO- decide if to check project ownership here
        if (!project.getUser().equals(currentUser)) {
            throw new SecurityException("Access denied");
        }
        Task task = TaskMapper.toEntity(taskDTO);
        task.setProject(project);
        task.setUser(currentUser);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }
}
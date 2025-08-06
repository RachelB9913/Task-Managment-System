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

    public List<TaskResponseDTO> getTasksByProjectIdAndUser(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        return project.getTasks()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Task> getTasks(Long projectId, User user) {
        return taskRepository.findAllByProjectIdAndUser(projectId, user);
    }

    public TaskResponseDTO getTaskById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return TaskMapper.toDTO(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO updatedTaskDTO, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        task.setTitle(updatedTaskDTO.getTitle());
        task.setDescription(updatedTaskDTO.getDescription());
        task.setStatus(updatedTaskDTO.getStatus());

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskRepository.delete(task);
    }

    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO taskDTO, User currentUser) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        Task task = TaskMapper.toEntity(taskDTO);
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }
}
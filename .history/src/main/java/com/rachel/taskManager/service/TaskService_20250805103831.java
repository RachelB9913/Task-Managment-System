package com.rachel.taskManager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.rachel.taskManager.repository.TaskRepository;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.repository.ProjectRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public List<TaskResponseDTO> getTasksByProjectId(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        // Assuming Project has getTasks() returning Collection<Task>
        return project.getTasks().values().stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Not found"));
        return TaskMapper.toDTO(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskResponseDTO updatedTaskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        // Use mapper to update entity from DTO
        TaskMapper.updateEntityFromDTO(task, updatedTaskDTO);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        taskRepository.delete(task);
    }

    public TaskResponseDTO createTask(Long projectId, TaskResponseDTO taskDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        Task task = TaskMapper.fromDTO(taskDTO);
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

}

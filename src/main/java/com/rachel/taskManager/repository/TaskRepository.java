package com.rachel.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByProjectIdAndUser(Long projectId, User user, Pageable pageable); // user-specific tasks in a project

    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);
    
    // For admin: get all tasks paginated
    Page<Task> findAll(Pageable pageable);
}

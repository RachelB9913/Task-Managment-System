package com.rachel.taskManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectIdAndUser(Long projectId, User user);

    
}

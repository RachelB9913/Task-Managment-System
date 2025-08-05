package com.rachel.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Task;

public class TaskRepository extends JpaRepository<Task, Long> {
    // This interface will automatically provide CRUD operations for Task entities
    // Additional custom query methods can be defined here if needed
    
}

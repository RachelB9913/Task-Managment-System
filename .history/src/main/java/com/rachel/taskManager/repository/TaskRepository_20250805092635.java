package com.rachel.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    
}

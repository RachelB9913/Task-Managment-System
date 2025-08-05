package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public class UserRepository extends JpaRepository<Project, Long> {
    // This class can be extended with custom query methods if needed
    
}

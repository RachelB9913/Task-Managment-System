package com.rachel.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	
}

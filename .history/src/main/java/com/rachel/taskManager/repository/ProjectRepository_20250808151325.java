package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByUser(User user, Pageable pageable);

}

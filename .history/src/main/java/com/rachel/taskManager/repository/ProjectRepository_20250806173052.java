package com.rachel.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Pageable pageable);

}

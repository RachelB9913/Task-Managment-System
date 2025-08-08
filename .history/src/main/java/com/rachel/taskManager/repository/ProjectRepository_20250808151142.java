package com.rachel.taskManager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByUser(User user, Pageable pageable);

}

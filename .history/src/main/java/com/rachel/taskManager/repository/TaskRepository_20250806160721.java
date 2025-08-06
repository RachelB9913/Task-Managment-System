package com.rachel.taskManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByProjectIdAndUser(Long projectId, User user, Pageable pageable);

}

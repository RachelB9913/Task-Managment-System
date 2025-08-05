package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
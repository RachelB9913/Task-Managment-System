package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCognitoSub(String cognitoSub);
}
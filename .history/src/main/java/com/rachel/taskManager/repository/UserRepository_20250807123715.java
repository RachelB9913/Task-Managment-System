package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findByMail(String mail);

    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findByCognitoSub(String cognitoSub);

    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findById(Long id);

    
    void deleteById(Long id);

}
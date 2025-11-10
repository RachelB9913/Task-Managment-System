package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find user by email with projects and tasks eagerly loaded
    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findByMail(String mail);

    // Find user by cognitoSub with projects and tasks eagerly loaded
    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findByCognitoSub(String cognitoSub);


    //without entity graph => retrieve only user details without projects and tasks - not used currently
    Optional<User> findUserByCognitoSub(String cognitoSub); 

}
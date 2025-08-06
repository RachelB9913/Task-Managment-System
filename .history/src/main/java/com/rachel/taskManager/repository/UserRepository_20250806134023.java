package com.rachel.taskManager.repository;

import com.rachel.taskManager.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"projects", "projects.tasks"})
    Optional<User> findByMail(String mail);
}
package com.rachel.taskManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rachel.taskManager.dto.ProjectDTO;
import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.service.UserService;
import com.rachel.taskManager.util.JwtUtils;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String sub = JwtUtils.extractSub(authentication);
        UserDTO user = userService.getCurrentUser(sub);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<List<ProjectDTO>> getProjectsForUser(@PathVariable Long id) {
        List<ProjectDTO> projects = userService.getProjectsForUser(id);
        return ResponseEntity.ok(projects);
    }
}

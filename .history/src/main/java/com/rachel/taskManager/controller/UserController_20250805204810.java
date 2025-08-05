package com.rachel.taskManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}

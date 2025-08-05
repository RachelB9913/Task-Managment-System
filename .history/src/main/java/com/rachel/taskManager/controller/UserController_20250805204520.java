package com.rachel.taskManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.UserService;
import com.rachel.taskManager.util.JwtUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // private final UserService userService;
    private final UserMapper userMapper;

    // @GetMapping("/me")
    // public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
    //     String sub = JwtUtils.extractSub(authentication);
    //     UserDTO user = userService.getCurrentUser(sub);
    //     return ResponseEntity.ok(user);
    // }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}

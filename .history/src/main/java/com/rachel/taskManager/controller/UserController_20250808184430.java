package com.rachel.taskManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.security.CurrentUser;
import com.rachel.taskManager.service.UserService;

import lombok.RequiredArgsConstructor;

import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_.logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser User user) {
        logger.info("User [{}] requested their own details", user.getCognitoSub());
        return ResponseEntity.ok(userService.getCurrentUserSummary(user.getCognitoSub()));
    }

    
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    // @GetMapping("/me/projects")
    // public ResponseEntity<PaginatedResponse<ProjectWithTaskSummaryDTO>> getProjectsForCurrentUser(
    //         @CurrentUser User user,
    //         Pageable pageable
    // ) {
    //     Page<ProjectWithTaskSummaryDTO> page = userService.getProjectsWithTasks(user.getCognitoSub(), pageable);

    //     PaginatedResponse<ProjectWithTaskSummaryDTO> response = new PaginatedResponse<>(
    //         page.getContent(),
    //         page.getNumber(),
    //         page.getSize(),
    //         page.getTotalElements(),
    //         page.getTotalPages(),
    //         page.isLast()
    //     );

    //     return ResponseEntity.ok(response);
    // }

}

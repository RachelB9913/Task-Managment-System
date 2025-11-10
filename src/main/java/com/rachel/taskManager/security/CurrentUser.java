package com.rachel.taskManager.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;


// This annotation will be used in controller method parameters
// to mark where the current user should be injected.

@Target({ ElementType.PARAMETER })  // Can be applied to method parameters
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime which means it can be accessed via reflection
@Documented                         // Included in Javadoc
@AuthenticationPrincipal            // Indicates that the annotated parameter should be resolved to the current authenticated user
public @interface CurrentUser {
}

package com.rachel.taskManager.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;


// This annotation will be used in controller method parameters
// to mark where the current user should be injected.

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}

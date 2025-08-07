package com.rachel.taskManager.security;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUserResolverTest {
    @Mock
    private UserService userService;
    @Mock
    private Jwt jwt;
    @Mock
    private JwtAuthenticationToken jwtAuth;
    @Mock
    private MethodParameter methodParameter;
    @Mock
    private SecurityContext securityContext;

    private AutoCloseable closeable;
    private CurrentUserResolver resolver;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        resolver = new CurrentUserResolver(userService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        SecurityContextHolder.clearContext();
    }

    @Test
    void resolveArgument_withJwtAuthenticationToken_returnsUser() throws Exception {
        User user = new User();
        when(jwtAuth.getToken()).thenReturn(jwt);
        when(userService.getOrCreateUserFromToken(jwt)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        SecurityContextHolder.setContext(securityContext);

        Object result = resolver.resolveArgument(methodParameter, null, null, null);
        assertSame(user, result);
    }

    @Test
    void resolveArgument_withInvalidAuthentication_throwsException() {
        Authentication auth = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            resolver.resolveArgument(methodParameter, null, null, null)
        );
        assertEquals("Invalid authentication type", ex.getMessage());
    }

    @Test
    void supportsParameter_returnsTrueForCurrentUserAnnotationAndUserType() {
        when(methodParameter.hasParameterAnnotation(CurrentUser.class)).thenReturn(true);
        doReturn((Class<?>) User.class).when(methodParameter).getParameterType();
        assertTrue(resolver.supportsParameter(methodParameter));
    }

    @Test
    void supportsParameter_returnsFalseForMissingAnnotation() {
        when(methodParameter.hasParameterAnnotation(CurrentUser.class)).thenReturn(false);
        doReturn((Class<?>) User.class).when(methodParameter).getParameterType();
        assertFalse(resolver.supportsParameter(methodParameter));
    }

    @Test
    void supportsParameter_returnsFalseForWrongType() {
        when(methodParameter.hasParameterAnnotation(CurrentUser.class)).thenReturn(true);
        doReturn((Class<?>) String.class).when(methodParameter).getParameterType();
        assertFalse(resolver.supportsParameter(methodParameter));
    }
}

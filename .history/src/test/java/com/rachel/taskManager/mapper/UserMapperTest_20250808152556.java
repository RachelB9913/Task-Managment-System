package com.rachel.taskManager.mapper;

import com.rachel.taskManager.dto.UserDTO;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final ProjectMapper projectMapper = Mockito.mock(ProjectMapper.class);
    private final UserMapper userMapper = new UserMapper(projectMapper);

    @Test
    void testToDTO() {
        User user = new User();
        user.setMail("test@mail.com");
        user.setAdmin(true);
        Project project = new Project(1L, "P1", "D1");
        user.setProjects(Set.of(project));
        Mockito.when(projectMapper.toSummaryDTO(project)).thenReturn(null); // Simplified for test
        UserDTO dto = userMapper.toDTO(user);
        assertEquals("test@mail.com", dto.getMail());
        assertTrue(dto.isAdmin());
        assertNotNull(dto.getProjects());
        assertEquals(1, dto.getProjects().size());
    }

    @Test
    void testToDTOWithNullProjects() {
        User user = new User();
        user.setMail("test@mail.com");
        user.setAdmin(false);
        user.setProjects(null);
        UserDTO dto = userMapper.toDTO(user);
        assertEquals("test@mail.com", dto.getMail());
        assertFalse(dto.isAdmin());
        assertNull(dto.getProjects());
    }

    @Test
    void testFromJwt() {
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn("sub123");
        Mockito.when(jwt.getClaimAsString("email")).thenReturn("user@mail.com");
        Mockito.when(jwt.getClaimAsBoolean("admin")).thenReturn(true);
        User user = userMapper.fromJwt(jwt);
        assertEquals("sub123", user.getCognitoSub());
        assertEquals("user@mail.com", user.getMail());
        assertTrue(user.isAdmin());
    }

    @Test
    void testFromJwtWithNullAdmin() {
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn("sub456");
        Mockito.when(jwt.getClaimAsString("email")).thenReturn("user2@mail.com");
        Mockito.when(jwt.getClaimAsBoolean("admin")).thenReturn(null);
        User user = userMapper.fromJwt(jwt);
        assertEquals("sub456", user.getCognitoSub());
        assertEquals("user2@mail.com", user.getMail());
        assertFalse(user.isAdmin());
    }
}

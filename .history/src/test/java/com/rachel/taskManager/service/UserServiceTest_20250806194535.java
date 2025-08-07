package com.rachel.taskManager.service;

import com.rachel.taskManager.dto.*;
import com.rachel.taskManager.mapper.ProjectMapper;
import com.rachel.taskManager.mapper.UserMapper;
import com.rachel.taskManager.model.Project;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.ProjectRepository;
import com.rachel.taskManager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserMapper userMapper;
    @Mock private ProjectMapper projectMapper;

    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User("abc123", "user@example.com");
        user.setAdmin(false);
    }

    @Test
    public void testGetCurrentUser_Success() {
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));
        UserDTO dto = new UserDTO();
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.getCurrentUser("abc123");
        assertNotNull(result);
        verify(userRepository).findByCognitoSub("abc123");
        verify(userMapper).toDTO(user);
    }

    @Test
    public void testGetCurrentUser_NotFound() {
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getCurrentUser("abc123");
        });
    }

    @Test
    public void testGetProjectsForUser_Success() {
        Project project = new Project(1L, "Test", "Desc");
        user.setProjects(Set.of(project));
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));
        when(projectMapper.toDTO(project)).thenReturn(new ProjectResponseDTO());

        List<ProjectResponseDTO> result = userService.getProjectsForUser("abc123");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrCreateUserFromToken_NewUser() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn("abc123");
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.empty(), Optional.of(user));
        when(userMapper.fromJwt(jwt)).thenReturn(user);

        User result = userService.getOrCreateUserFromToken(jwt);
        assertEquals("abc123", result.getCognitoSub());
        verify(userRepository).save(user);
    }

    @Test
    public void testGetCurrentUserSummary_Success() {
        Project project = new Project(1L, "P", "D");
        user.setProjects(Set.of(project));
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));

        UserDTO dto = userService.getCurrentUserSummary("abc123");

        assertNotNull(dto);
        assertEquals("user@example.com", dto.getMail());
    }

    @Test
    public void testGetProjectsWithTasks_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Project project = new Project(1L, "P", "D");
        project.setTasks(new HashSet<>());

        Page<Project> page = new PageImpl<>(List.of(project));
        when(userRepository.findByCognitoSub("abc123")).thenReturn(Optional.of(user));
        when(projectRepository.findByUser(user, pageable)).thenReturn(page);

        Page<ProjectWithTaskSummaryDTO> result = userService.getProjectsWithTasks("abc123", pageable);
        assertEquals(1, result.getContent().size());
    }
}

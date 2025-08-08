package com.rachel.taskManager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.TaskService;
import com.rachel.taskManager.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class AdminControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProjects_ReturnsPaginatedResponse() {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        List<ProjectResponseDTO> dtos = Collections.singletonList(dto);
        Page<ProjectResponseDTO> page = new PageImpl<>(dtos, PageRequest.of(0, 10), 1);
        when(projectService.getAllProjects(any(Pageable.class))).thenReturn(page);

        ResponseEntity<PaginatedResponse<ProjectResponseDTO>> response = adminController.getAllProjects(PageRequest.of(0, 10));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
    }

     @Test
    void getAllProjects_authenticated_returnsOk() throws Exception {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(1L);
        dto.setName("Test Project");
        dto.setDescription("Test Desc");

        Page<ProjectResponseDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(projectService.getAllProjects(any())).thenReturn(page);

        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(get("/api/projects/all?page=0&size=10")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }
}
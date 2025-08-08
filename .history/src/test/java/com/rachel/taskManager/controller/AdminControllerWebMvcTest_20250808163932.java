package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.TaskService;
import com.rachel.taskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
class AdminControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private UserService userService;
    

    @Test
    void getAllProjects_authenticated_returnsOk() throws Exception {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(1L);
        dto.setName("Test Project");
        dto.setDescription("Test Desc");

        Page<ProjectResponseDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(projectService.getAllProjects(any())).thenReturn(page);

        Jwt jwt = Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
        mockMvc.perform(get("/api/admin/projects/all?page=0&size=10")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }
}

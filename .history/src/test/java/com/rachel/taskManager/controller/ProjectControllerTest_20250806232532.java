package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.ProjectRequestDTO;
import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProjectController.class)
@Import(MockCurrentUserResolverConfig.class) // Make sure this config exists and is correct
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private com.rachel.taskManager.service.UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateProject() throws Exception {
        ProjectRequestDTO requestDTO = new ProjectRequestDTO();
        requestDTO.setName("Test Project");
        requestDTO.setDescription("Test Desc");

        ProjectResponseDTO responseDTO = new ProjectResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Project");
        responseDTO.setDescription("Test Desc");

        Mockito.when(projectService.createProject(any(ProjectRequestDTO.class), any(User.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/projects")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"))
                .andExpect(jsonPath("$.description").value("Test Desc"));
    }
}
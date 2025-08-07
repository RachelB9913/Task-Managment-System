package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.TaskRequestDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(TaskController.class)
class TaskControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private com.rachel.taskManager.service.UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Jwt jwt() {
        return Jwt.withTokenValue("token").header("alg", "none").claim("sub", "testuser").build();
    }

    private User mockUser() {
        return new User();
    }

    @Test
    void getTaskById_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        Long taskId = 2L;
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(taskId);
        when(taskService.getTaskById(eq(taskId), any())).thenReturn(response);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(get("/api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));
    }

    @Test
    void getTasks_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(2L);
        Page<TaskResponseDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(taskService.getTasksByProjectIdAndUser(eq(projectId), any(), any())).thenReturn(page);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(get("/api/projects/{projectId}/tasks?page=0&size=10", projectId)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2L));
    }

    @Test
    void createTask_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        TaskRequestDTO request = new TaskRequestDTO();
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(2L);
        when(taskService.createTask(eq(projectId), any(), any())).thenReturn(response);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(post("/api/projects/{projectId}/tasks", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void updateTask_authenticated_returnsOk() throws Exception {
        Long projectId = 1L;
        Long taskId = 2L;
        TaskRequestDTO request = new TaskRequestDTO();
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(taskId);
        when(taskService.updateTask(eq(taskId), any(), any())).thenReturn(response);
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(put("/api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));
    }

    @Test
    void deleteTask_authenticated_returnsNoContent() throws Exception {
        Long projectId = 1L;
        Long taskId = 2L;
        Mockito.doNothing().when(taskService).deleteTask(eq(taskId), any());
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());
        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTaskById_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/projects/1/tasks/2"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getTaskById_notFound_returnsNotFound() throws Exception {
        when(taskService.getTaskById(eq(2L), any()))
            .thenThrow(new NoSuchElementException("Task not found"));
        when(userService.getOrCreateUserFromToken(any())).thenReturn(mockUser());

        mockMvc.perform(get("/api/projects/1/tasks/2")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }


}

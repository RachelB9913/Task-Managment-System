package com.rachel.taskManager.controller;

import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;
    private User mockUser;
    private TaskResponseDTO taskDTO;

    @BeforeEach
    void setup() {
        taskService = mock(TaskService.class);

        TaskController taskController = new TaskController(taskService);

        // Setup mock user
        mockUser = new User();
        mockUser.setCognitoSub("mock-sub");
        mockUser.setMail("mock@example.com");

        // Sample task
        taskDTO = new TaskResponseDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Task One");

        Pageable mockPageable = PageRequest.of(0, 10);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setCustomArgumentResolvers(
                    new HandlerMethodArgumentResolver() {
                        @Override
                        public boolean supportsParameter(MethodParameter parameter) {
                            return parameter.getParameterType().equals(User.class);
                        }

                        @Override
                        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                            return mockUser;
                        }
                    },
                    new HandlerMethodArgumentResolver() {
                        @Override
                        public boolean supportsParameter(MethodParameter parameter) {
                            return Pageable.class.equals(parameter.getParameterType());
                        }

                        @Override
                        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                            return PageRequest.of(0, 10); // default test value
                        }
                    })
                .build();
    }

    @Test
    public void testGetTasks_Success() throws Exception {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(1L);
        dto.setTitle("Test Task");

        // Spy on a PageImpl so we can override behavior if needed
        Page<TaskResponseDTO> page = spy(new PageImpl<>(List.of(dto)));

        when(taskService.getTasksByProjectIdAndUser(eq(42L), eq(mockUser), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/projects/42/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Task"));
    }
}

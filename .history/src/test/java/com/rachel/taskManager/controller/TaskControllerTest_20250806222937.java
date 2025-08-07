package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.mapper.TaskMapper;
import com.rachel.taskManager.model.Task;
import com.rachel.taskManager.model.User;
import com.rachel.taskManager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;
    private TaskMapper taskMapper;
    private User mockUser;
    private Task task;
    private TaskResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        taskService = mock(TaskService.class);
        taskMapper = mock(TaskMapper.class);

        mockUser = new User();
        mockUser.setCognitoSub("mock-sub");
        mockUser.setMail("mock@example.com");

        TaskController taskController = new TaskController(taskService, taskMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(User.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter,
                                                ModelAndViewContainer mavContainer,
                                                NativeWebRequest webRequest,
                                                WebDataBinderFactory binderFactory) {
                        return mockUser;
                    }
                }).build();

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        responseDTO = new TaskResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Test Task");
    }


    // helper class to simulate @CurrentUser
    static class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().equals(User.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            User user = new User();
            user.setCognitoSub("mock-sub");
            user.setMail("mock@example.com");
            return user;
        }
    }

    @Test
    public void testGetTaskById_Success() throws Exception {
        when(taskService.getTaskById(1L, mockUser)).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/projects/1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }
}

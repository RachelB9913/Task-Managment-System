package com.rachel.taskManager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import com.rachel.taskManager.dto.ProjectResponseDTO;
import com.rachel.taskManager.dto.TaskResponseDTO;
import com.rachel.taskManager.service.ProjectService;
import com.rachel.taskManager.service.TaskService;
import com.rachel.taskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;


    // No need for setUp with @WebMvcTest and @MockBean


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProjects_ReturnsPaginatedResponse() throws Exception {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(1L);
        dto.setName("Test Project");
        dto.setDescription("Test Desc");
        Page<ProjectResponseDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(projectService.getAllProjects(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admin/projects/all?page=0&size=10"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTasks_ReturnsPaginatedResponse() throws Exception {
        TaskResponseDTO dto = new TaskResponseDTO();
        Page<TaskResponseDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(taskService.getAllTasks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admin/tasks/all?page=0&size=10"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    void getAllProjects_NonAdminUser_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/projects/all?page=0&size=10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllTasks_NonAdminUser_Forbidden() throws Exception {
        mockMvc.perform(get("/api/admin/tasks/all?page=0&size=10"))
                .andExpect(status().isForbidden());
    }
    
}
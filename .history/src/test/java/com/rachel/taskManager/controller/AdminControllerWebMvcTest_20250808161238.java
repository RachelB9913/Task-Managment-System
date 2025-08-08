package com.rachel.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rachel.taskManager.dto.UpdateRoleRequest;
import com.rachel.taskManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_shouldReturnOk() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/admin/users/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("sub123");
        mockMvc.perform(delete("/api/admin/users/sub123").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_shouldReturnNoContent() throws Exception {
        UpdateRoleRequest req = new UpdateRoleRequest();
        req.setNewRole("ADMIN");
        Mockito.doNothing().when(userService).updateUserRole(eq("sub123"), eq("ADMIN"));
        mockMvc.perform(put("/api/admin/users/sub123/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

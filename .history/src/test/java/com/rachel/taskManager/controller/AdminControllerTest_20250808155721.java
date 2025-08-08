package com.rachel.taskManager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.rachel.taskManager.dto.PaginatedResponse;
import com.rachel.taskManager.dto.ProjectResponseDTO;

public class AdminControllerTest {

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProjects_ReturnsPaginatedResponse() {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        List<ProjectResponseDTO> dtos = Collections.singletonList(dto);
        Page<ProjectResponseDTO> page = new PageImpl<>(dtos, PageRequest.of(0, 10), 1);
        when(projectService.getAllProjects(any(Pageable.class))).thenReturn(page);

        ResponseEntity<PaginatedResponse<ProjectResponseDTO>> response = projectController.getAllProjects(PageRequest.of(0, 10));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
    }
    
}

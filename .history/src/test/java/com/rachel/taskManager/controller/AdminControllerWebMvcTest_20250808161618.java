package com.rachel.taskManager.controller;

public class AdminControllerWebMvcTest  {
    
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

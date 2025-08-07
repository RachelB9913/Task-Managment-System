package com.rachel.taskManager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {
    @Test
    void testNoArgsConstructor() {
        UserDTO dto = new UserDTO();
        assertNotNull(dto);
    }
}

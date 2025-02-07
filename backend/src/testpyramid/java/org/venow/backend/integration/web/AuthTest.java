package org.venow.backend.integration.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.web.AuthController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should return 401 when unauthenticated")
    void testHealthUnauthenticated() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 200 when authenticated")
    @WithMockUser
    void testHealthAuthenticated() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated"));
    }
}

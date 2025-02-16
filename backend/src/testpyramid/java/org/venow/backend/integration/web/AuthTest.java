package org.venow.backend.integration.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.config.TestSecurityConfig;
import org.venow.backend.service.UserRepository;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.web.AuthController;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return 401 when unauthenticated")
    void testHealthUnauthenticated() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Should return 200 when authenticated")
    @WithMockUser
    void testHealthAuthenticated() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated"));
    }

    @Test
    @DisplayName("Should login successfully and redirect as Organizer")
    void testLoginSuccessOrganizer() throws Exception {
        UUID organizerId = UUID.randomUUID();
        UserDTO testUser = new UserDTO(1L,
                organizerId,
                "Test",
                new BCryptPasswordEncoder().encode("password123"),
                "organizer3@example.com",
                "ORGANIZER"
        );
        when(userRepository.findByEmail("organizer3@example.com"))
                .thenReturn(Optional.of(testUser));

        String response = mockMvc.perform(post("/login")
                        .param("username", "organizer3@example.com")
                        .param("password", "password123"))
                .andReturn().getResponse().getHeader("Location");
        assertThat(response).contains("/organizer/" + organizerId + "/events");
    }

    @Test
    @DisplayName("Should login successfully and redirect as Exhibitor")
    void testLoginSuccessExhibitor() throws Exception {
        UUID organizerId = UUID.randomUUID();
        UserDTO testUser = new UserDTO(1L,
                organizerId,
                "Test",
                new BCryptPasswordEncoder().encode("password123"),
                "organizer3@example.com",
                "EXHIBITOR"
        );
        when(userRepository.findByEmail("organizer3@example.com"))
                .thenReturn(Optional.of(testUser));

        String response = mockMvc.perform(post("/login")
                        .param("username", "organizer3@example.com")
                        .param("password", "password123"))
                .andReturn().getResponse().getHeader("Location");
        assertThat(response).contains("/exhibitor/" + organizerId + "/events");
    }

    @Test
    @DisplayName("Should not login successfully and get Error")
    void testLoginFail() throws Exception {
        when(userRepository.findByEmail("organizer3@example.com"))
                .thenReturn(Optional.empty());

        String redirectUrl = mockMvc.perform(post("/login")
                        .param("username", "organizer3@example.com")
                        .param("password", "password123"))
                .andReturn().getResponse().getHeader("Location");
        assertThat(redirectUrl).isEqualTo("/login?error");
    }
}


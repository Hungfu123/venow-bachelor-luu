package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.venow.backend.domain.model.Role;
import org.venow.backend.infrastructure.UserRepositoryImpl;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("should return an user by userId")
    @Test
    void shouldReturnUserByUserId() {
        UUID userId = UUID.randomUUID();
        UserDTO user = UserDTOFactory.createDefault().withUserId(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByUserId(userId);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Luugner");
        assertThat(result.email()).isEqualTo("luugner@venow.com");
        assertThat(result.role()).isEqualTo("ORGANIZER");


    }



    @DisplayName("should return empty optional if user is not found")
    @Test
    void shouldReturnEmptyOptionalIfUserNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<UserDTO> retrievedUser = userRepository.findById(nonExistentId);
        assertThat(retrievedUser).isEmpty();
    }

    @DisplayName("should throw NotFoundException if user is not found")
    @Test
    void shouldThrowNotFoundExceptionIfUserNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        when(userRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUserId(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found for ID: " + nonExistentId);
    }


    @DisplayName("should return true if user is an organizer")
    @Test
    void shouldReturnTrueIfUserIsOrganizer() {
        UUID organizerId = UUID.randomUUID();

        when(userRepository.existsByUserIdAndRole(organizerId, Role.ORGANIZER)).thenReturn(true);

        boolean result = userService.existsByUserAndRole(organizerId, Role.ORGANIZER);

        assertThat(result).isTrue();
    }

    @DisplayName("should return false if user is not an organizer")
    @Test
    void shouldReturnFalseIfUserIsNotOrganizer() {
        UUID nonOrganizerId = UUID.randomUUID();

        when(userRepository.existsByUserIdAndRole(nonOrganizerId, Role.EXHIBITOR)).thenReturn(false);

        boolean result = userService.existsByUserAndRole(nonOrganizerId, Role.EXHIBITOR);

        assertThat(result).isFalse();
    }

    @DisplayName("should load user by username")
    @Test
    void shouldLoadUserByUsername() {
        String email = "test@venow.com";
        UUID userId = UUID.randomUUID();
        String password = "securePassword";
        Role role = Role.ORGANIZER;

        UserDTO user = new UserDTO(1L,userId, "Test User", password, email , role.toString());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ORGANIZER");
    }

    @DisplayName("should throw UsernameNotFoundException when user does not exist")
    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        String email = "nonexistent@venow.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with email: " + email);
    }
}
package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.infrastructure.UserRepositoryImpl;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

        when(userRepository.findById(nonExistentId).isEmpty()).thenThrow(new NotFoundException("User not found"));

        assertThatThrownBy(() -> userService.getUserByUserId(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @DisplayName("should return true if user is an organizer")
    @Test
    void shouldReturnTrueIfUserIsOrganizer() {
        UUID organizerId = UUID.randomUUID();

        when(userRepository.existsByOrganizerId(organizerId)).thenReturn(true);

        boolean result = userService.existsByOrganizerId(organizerId);

        assertThat(result).isTrue();
    }

    @DisplayName("should return false if user is not an organizer")
    @Test
    void shouldReturnFalseIfUserIsNotOrganizer() {
        UUID nonOrganizerId = UUID.randomUUID();

        when(userRepository.existsByOrganizerId(nonOrganizerId)).thenReturn(false);

        boolean result = userService.existsByOrganizerId(nonOrganizerId);

        assertThat(result).isFalse();
    }
}
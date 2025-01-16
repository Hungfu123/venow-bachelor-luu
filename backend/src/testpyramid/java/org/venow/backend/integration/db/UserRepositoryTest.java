package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.domain.model.Role;
import org.venow.backend.infrastructure.UserRepositoryImpl;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class UserRepositoryTest extends TestBase {

    @Autowired
    private UserRepositoryImpl userRepository;

    @DisplayName("should save organizer and retrieve user by ID")
    @Test
    void shouldRetrieveUserById() {
        UserDTO user = UserDTOFactory.createDefault().build();
        userRepository.save(user);

        Optional<UserDTO> retrievedUser = userRepository.findById(user.userId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().name()).isEqualTo(user.name());
        assertThat(retrievedUser.get().email()).isEqualTo(user.email());
        assertThat(retrievedUser.get().role()).isEqualTo(user.role());

    }

    @DisplayName("should return empty optional if user is not found")
    @Test
    void shouldReturnEmptyOptionalIfUserNotFound() {
        Optional<UserDTO> retrievedUser = userRepository.findById(UUID.randomUUID());

        assertThat(retrievedUser).isEmpty();
    }

    @DisplayName("should return true if user exists by organizer ID")
    @Test
    
    void shouldReturnTrueIfUserExistsByUserIdAndRole() {
        UserDTO user = UserDTOFactory.createDefault().build();
        userRepository.save(user);

        boolean exists = userRepository.existsByUserIdAndRole(user.userId(), Role.ORGANIZER);

        assertThat(exists).isTrue();
    }

    @DisplayName("should return false if user does not exist by organizer ID")
    @Test
    void shouldReturnFalseIfUserDoesNotExistByOrganizerId() {
        boolean exists = userRepository.existsByUserIdAndRole(UUID.randomUUID(), Role.ORGANIZER);

        assertThat(exists).isFalse();
    }

    @DisplayName("should return false if user is exhibitor")
    @Test
    void shouldReturnFalseIfUserIsExhibitor() {
        UserDTO user = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        userRepository.save(user);

        boolean exists = userRepository.existsByUserIdAndRole(user.userId(), Role.ORGANIZER);

        assertThat(exists).isFalse();
    }
}

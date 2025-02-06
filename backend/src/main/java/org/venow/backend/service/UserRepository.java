package org.venow.backend.service;

import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.UserDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UserDTO> findById(final UUID id);

    Optional<UserDTO> findByEmail(final String email);
    UserDTO save(UserDTO user);

    boolean existsByUserIdAndRole(UUID userId, Role role);

}

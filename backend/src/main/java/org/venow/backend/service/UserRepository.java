package org.venow.backend.service;

import org.venow.backend.domain.model.User;
import org.venow.backend.service.dto.UserDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UserDTO> findById(final UUID id);

    UserDTO save(UserDTO user);

    boolean existsByOrganizerId(UUID organizerId);
}

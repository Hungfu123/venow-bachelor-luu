package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.venow.backend.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserId(UUID userId);

}

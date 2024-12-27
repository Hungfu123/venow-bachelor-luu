package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM users u WHERE u.userId = :userId")
    Optional<User> findByUserId(UUID userId);

    boolean existsByUserIdAndRole(UUID organizerId, Role role);
}

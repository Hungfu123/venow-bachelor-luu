package org.venow.backend.infrastructure;
import org.springframework.data.jpa.repository.JpaRepository;

import org.venow.backend.domain.model.Application;

import java.util.Optional;
import java.util.UUID;

public interface JpaApplicationRepository extends JpaRepository<Application, UUID> {
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);

    Optional<Application> findByApplicationId(UUID applicationId);
}

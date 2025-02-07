package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.venow.backend.domain.model.Application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaApplicationRepository extends JpaRepository<Application, UUID> {
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);

    Optional<Application> findByApplicationId(UUID applicationId);

    @Query("SELECT a FROM applications a JOIN events e ON a.eventId = e.eventId WHERE e.organizerId =:organizerId")
    List<Application> findAllByOrganizerIdFromEvent(UUID organizerId);

    @Query("SELECT a FROM applications a JOIN events e ON a.eventId = e.eventId WHERE e.organizerId =:organizerId AND e.eventId =:eventId")
    List<Application> findAllByOrganizerIdAndEventId(UUID organizerId, UUID eventId);

    @Query("SELECT e.title FROM events e WHERE e.eventId =:eventId")
    String findEventNameByEventId(UUID eventId);

    List<Application> findAllByUserId(UUID exhibitorId);
}

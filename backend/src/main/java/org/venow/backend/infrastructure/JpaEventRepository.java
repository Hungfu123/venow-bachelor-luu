package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.venow.backend.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findEventByEventId(UUID id);

    List<Event> findAll();

    List<Event> findAllByOrganizerId(UUID organizerId);

    @Query("SELECT e FROM events e WHERE UPPER(e.title) LIKE CONCAT('%', UPPER(:title), '%')")
    List<Event> findAllByTitleContainingIgnoreCase(@Param("title") String title);
}

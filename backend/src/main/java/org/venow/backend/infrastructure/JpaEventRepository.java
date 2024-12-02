package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.venow.backend.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findEventByEventId(UUID id);

    List<Event> findAll();
}

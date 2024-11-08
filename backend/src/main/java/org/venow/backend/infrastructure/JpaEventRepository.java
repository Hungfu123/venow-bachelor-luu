package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.venow.backend.domain.model.Event;

import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<Event, UUID> {
}

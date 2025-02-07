package org.venow.backend.service;

import org.venow.backend.service.dto.EventDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    List<EventDTO> findAll();

    Optional<EventDTO> findById(UUID id);

    EventDTO save(EventDTO event);

    List<EventDTO> findAllByOrganizerId(UUID organizerId);

    List<EventDTO> findAllByTitleContaining(String query);
}

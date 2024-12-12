package org.venow.backend.applicationservice;

import org.venow.backend.applicationservice.dto.EventDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    List<EventDTO> findAll();

    Optional<EventDTO> findById(UUID id);

    EventDTO save(EventDTO event);

}

package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.dto.EventDTO;
import org.venow.backend.applicationservice.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Eventservice {

    private final EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll();

    }

    public EventDTO getEvent(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
    }
}

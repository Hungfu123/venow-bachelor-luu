package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.exception.NotFoundException;
import org.venow.backend.applicationservice.mapper.EventMapper;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Eventservice {

    private final EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::mapToEventDTO)
                .toList();

    }

    public EventDTO getEvent(UUID eventId) {
        return EventMapper.mapToEventDTO(eventRepository.findById(eventId).orElseThrow(NotFoundException::new));
    }
}

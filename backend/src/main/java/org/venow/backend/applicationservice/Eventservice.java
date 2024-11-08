package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.mapper.EventMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class Eventservice {
//TODO:
    private final EventRepository eventRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::mapToEventDTO)
                .toList();

    }

}

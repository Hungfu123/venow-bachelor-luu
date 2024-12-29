package org.venow.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Eventservice {

    private final EventRepository eventRepository;
    private final UserService userService;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll();

    }

    public EventDTO getEvent(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public List<EventDTO> getAllEventsByOrganizer(UUID organizerId, Role role) {
        if(!userService.existsByUserAndRole(organizerId, role)) {
            throw new NotFoundException(role.name() + "Id not found");
        }

        return eventRepository.findAllByOrganizerId(organizerId).stream().map(event -> {
            UserDTO organizer = userService.getUserByUserId(event.organizerId());
            return new EventDTO(event.eventId(), event.title(), event.description(), event.location(), event.startDate(), event.endDate(), organizer.name(), event.organizerId());

        }).toList();
    }
}

package org.venow.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Eventservice {

    private final EventRepository eventRepository;
    private final UserService userService;

    public List<EventDTO> getAllEvents() {

        return eventRepository.findAll().stream().map(event -> {
            UserDTO organizer = userService.getUserByUserId(event.organizerId());
            return new EventDTO(event.id(), event.eventId(), event.title(), event.description(), event.location(), event.startDate(), event.endDate(), organizer.name(), event.booths(), event.organizerId());
        }).toList();

    }

    public EventDTO getEvent(final UUID eventId) {
        final EventDTO event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        final UserDTO organizer = userService.getUserByUserId(event.organizerId());
        return new EventDTO(event.id(), event.eventId(), event.title(), event.description(), event.location(), event.startDate(), event.endDate(), organizer.name(), event.booths(), event.organizerId());
    }

    public List<EventDTO> getAllEventsByOrganizer(final UUID organizerId, final Role role) {
        if (!userService.existsByUserAndRole(organizerId, role)) {
            throw new NotFoundException(role.name() + "Id not found");
        }

        return eventRepository.findAllByOrganizerId(organizerId).stream().map(event -> {
            UserDTO organizer = userService.getUserByUserId(event.organizerId());
            return new EventDTO(event.id(), event.eventId(), event.title(), event.description(), event.location(), event.startDate(), event.endDate(), organizer.name(), event.booths(), event.organizerId());

        }).toList();
    }

    public EventDTO createEvent(final EventDTO event, final UUID organizerId) {
        if (!userService.existsByUserAndRole(organizerId, Role.ORGANIZER)) {
            throw new NotFoundException("OrganizerId not found");
        }
        final UserDTO organizer = userService.getUserByUserId(organizerId);
        final EventDTO savedEvent = eventRepository.save(event);

        return EventDTO.builder()
                .eventId(savedEvent.eventId())
                .title(savedEvent.title())
                .description(savedEvent.description())
                .location(savedEvent.location())
                .startDate(savedEvent.startDate())
                .endDate(savedEvent.endDate())
                .organizerName(organizer.name())
                .organizerId(savedEvent.organizerId())
                .booths(savedEvent.booths())
                .build();
    }

    public EventDTO updateEvent(EventDTO event, UUID organizerId) {
        if (!userService.existsByUserAndRole(organizerId, Role.ORGANIZER)) {
            throw new NotFoundException("OrganizerId not found");
        }
        final EventDTO retrievedEvent = getEvent(event.eventId());
        final UserDTO organizer = userService.getUserByUserId(organizerId);
        final EventDTO eventNew = EventDTO.builder()
                .id(retrievedEvent.id())
                .eventId(event.eventId())
                .title(event.title())
                .description(event.description())
                .location(event.location())
                .startDate(event.startDate())
                .endDate(event.endDate())
                .organizerName(organizer.name())
                .booths(event.booths())
                .organizerId(event.organizerId()).build();

        final EventDTO updatedEvent = eventRepository.save(eventNew);
        return new EventDTO(updatedEvent.id(), updatedEvent.eventId(), updatedEvent.title(), updatedEvent.description(), updatedEvent.location(), updatedEvent.startDate(), updatedEvent.endDate(), organizer.name(), updatedEvent.booths(), updatedEvent.organizerId());
    }

    public List<EventDTO> getEventsBySearchTitle(String query) {
        if (query.length() < 3) {
            return Collections.emptyList();
        }
        return eventRepository.findAllByTitleContaining(query).stream()
                .map(event ->
                        EventDTO.builder()
                                .eventId(
                                        event.eventId())
                                .title(event.title())
                                .description(event.description())
                                .location(event.location())
                                .startDate(event.startDate())
                                .endDate(event.endDate())
                                .organizerId(event.organizerId())
                                .organizerName(userService.getUserByUserId(event.organizerId()).name())
                                .booths(event.booths())
                                .build())
                .toList();
    }

}



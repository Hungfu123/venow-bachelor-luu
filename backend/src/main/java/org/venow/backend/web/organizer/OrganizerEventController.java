package org.venow.backend.web.organizer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.web.reponse.EventResponse;
import org.venow.backend.web.reponse.ResponseMapper;
import org.venow.backend.web.request.EventRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
@PreAuthorize("#organizerId == authentication.principal.userId")
public class OrganizerEventController {

    private final Eventservice eventservice;
    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEventsByOrganizer(@PathVariable("organizerId") final UUID organizerId) {
        final List<EventDTO> allEvents = eventservice.getAllEventsByOrganizer(organizerId, Role.ORGANIZER);
        log.info("Found all events from organizer{}", organizerId);
        List<EventResponse> list = allEvents.stream().map(ResponseMapper::mapToEventResponse).toList();
        return ResponseEntity.ok(list);
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> getEventDetails(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("eventId") final UUID eventId) {
        final EventDTO eventDTO = eventservice.getEvent(eventId);
        return ResponseEntity.ok(ResponseMapper.mapToEventResponse(eventDTO));
    }

    @GetMapping("/events/edit/{eventId}")
    public ResponseEntity<EventResponse> editEventFormular(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("eventId") final UUID eventId) {
        final EventDTO eventDTO = eventservice.getEvent(eventId);
        return ResponseEntity.ok(ResponseMapper.mapToEventResponse(eventDTO));
    }

    @PostMapping("/events/edit/{eventId}")
    public ResponseEntity<EventResponse> editEvent(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("eventId") final UUID eventId,
            @Valid @RequestBody final EventRequest request) {

        final EventDTO eventDTO = EventDTO.builder()
                .eventId(eventId)
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .organizerId(organizerId)
                .booths(request.booths())
                .build();

        final EventDTO updatedEvent = eventservice.updateEvent(eventDTO, organizerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMapper.mapToEventResponse(updatedEvent));
    }

    @GetMapping("/events/create")
    public ResponseEntity<EventResponse> createEventFormular(
            @PathVariable("organizerId") final UUID organizerId) {
        return ResponseEntity.ok(EventResponse.builder().build());
    }

    @PostMapping("/events/create")
    public ResponseEntity<EventResponse> createEvent(
            @PathVariable("organizerId") final UUID organizerId,
            @Valid @RequestBody final EventRequest request) {

        final EventDTO eventDTO = EventDTO.builder()
                .eventId(UUID.randomUUID())
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .booths(request.booths())
                .organizerId(organizerId)
                .build();

        final EventDTO createdEvent = eventservice.createEvent(eventDTO, organizerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMapper.mapToEventResponse(createdEvent));
    }

}

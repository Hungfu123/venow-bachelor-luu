package org.venow.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.web.reponse.EventResponse;
import org.venow.backend.web.reponse.ResponseMapper;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
public class OrganizerEventController {

    private final Eventservice eventservice;

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEventsByOrganizer(@PathVariable("organizerId") final UUID organizerId) {
        final List<EventDTO> allEvents = eventservice.getAllEventsByOrganizer(organizerId, Role.ORGANIZER);
        log.info("Found all events from organizer{}", organizerId);
        return ResponseEntity.ok(allEvents.stream().map(ResponseMapper::mapToEventResponse).toList());
    }
}

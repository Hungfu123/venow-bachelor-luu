package org.venow.backend.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.Eventservice;
import org.venow.backend.domain.model.Event;
import org.venow.backend.web.reponse.EventResponse;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class EventController {

    private final Eventservice eventservice;

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        final List<EventDTO> allEvents = eventservice.getAllEvents();
        log.info("Found all events");
        return ResponseEntity.ok(allEvents.stream().map(event -> mapToEventResponse(event)).toList());
    }


    private EventResponse mapToEventResponse(final EventDTO event) {
        return EventResponse.builder()
                .eventId(event.eventId())
                .title(event.title())
                .description(event.description())
                .location(event.location())
                .startDate(event.startDate())
                .endDate(event.endDate())
                .build();
    }
}

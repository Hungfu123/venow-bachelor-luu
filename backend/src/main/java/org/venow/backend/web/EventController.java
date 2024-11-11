package org.venow.backend.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.Eventservice;
import org.venow.backend.web.reponse.EventResponse;
import org.venow.backend.web.reponse.ResponseMapper;

import java.util.List;

import static org.venow.backend.web.reponse.ResponseMapper.mapToEventResponse;

@Slf4j
@RestController
@AllArgsConstructor
public class EventController {

    private final Eventservice eventservice;

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        final List<EventDTO> allEvents = eventservice.getAllEvents();
        log.info("Found all events");
        return ResponseEntity.ok(allEvents.stream().map(ResponseMapper::mapToEventResponse).toList());
    }



}

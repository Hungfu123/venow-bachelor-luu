package org.venow.backend.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.Eventservice;
import org.venow.backend.web.reponse.EventResponse;

import java.util.UUID;

import static org.venow.backend.web.reponse.ResponseMapper.mapToEventResponse;
@Slf4j
@RestController()
@RequestMapping("/showman/{exhibitorId}")
public class ExhibitorController {

    private final Eventservice eventservice;

    public ExhibitorController(Eventservice eventservice) {
        this.eventservice = eventservice;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventResponse> getEventDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable ("eventId") final UUID eventId) {
        EventDTO eventDTO = eventservice.getEvent(eventId);
        log.info("Found event {}", eventDTO.eventId());
        return ResponseEntity.ok(mapToEventResponse(eventDTO));
    }


}

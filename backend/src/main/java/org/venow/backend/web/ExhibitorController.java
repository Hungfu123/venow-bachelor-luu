package org.venow.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.Applicationservice;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.Eventservice;
import org.venow.backend.web.reponse.ApplicationResponse;
import org.venow.backend.web.reponse.EventResponse;
import org.venow.backend.web.request.ApplicationRequest;

import java.util.UUID;

import static org.venow.backend.web.reponse.ResponseMapper.mapToApplicationResponse;
import static org.venow.backend.web.reponse.ResponseMapper.mapToEventResponse;
@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/exhibitor/{exhibitorId}")
public class ExhibitorController {

    private final Eventservice eventservice;
    private final Applicationservice applicationservice;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventResponse> getEventDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("eventId") final UUID eventId) {
        final EventDTO eventDTO = eventservice.getEvent(eventId);
        log.info("Found event {}", eventDTO.eventId());
        return ResponseEntity.ok(mapToEventResponse(eventDTO));
    }

    @GetMapping("/event/{eventId}/application")
    public ResponseEntity<ApplicationResponse> getApplicationDetails(
            @PathVariable("eventId") final UUID eventId,
            @PathVariable("exhibitorId") final UUID exhibitorId
    ) {
        final ApplicationResponse response = ApplicationResponse.builder()
                .userId(exhibitorId)
                .eventId(eventId)
                .previousParticipation(false)
                .message("")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/event/{eventId}/application")
    public ResponseEntity<ApplicationResponse> applyForEvent(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("eventId") final UUID eventId,
            @RequestBody final ApplicationRequest applicationRequest
    ) {
        final ApplicationDTO applicationDTO = applicationservice.createApplication(ApplicationDTO
                .builder()
                        .applicationId(UUID.randomUUID())
                        .userId(exhibitorId)
                        .eventId(eventId)
                        .contactInformation(applicationRequest.contactInformation())
                        .businessName(applicationRequest.businessName())
                        .typeOfBusiness(applicationRequest.typeOfBusiness())
                        .previousParticipation(applicationRequest.previousParticipation())
                        .message(applicationRequest.message())
                        .status("IN_PROGRESS")
                        .build()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApplicationResponse(applicationDTO));
    }

}

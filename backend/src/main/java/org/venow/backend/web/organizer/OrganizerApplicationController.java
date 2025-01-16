package org.venow.backend.web.organizer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.web.reponse.ApplicationResponse;
import org.venow.backend.web.reponse.ResponseMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.venow.backend.web.reponse.ResponseMapper.mapToApplicationResponse;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
public class OrganizerApplicationController {

    @Autowired
    private final ApplicationService applicationservice;

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getallApplicationsByOrganizer(
            @PathVariable("organizerId") final UUID organizerId) {
        final List<ApplicationDTO> allApplication = applicationservice.getAllApplicationsByOrganizer(organizerId);
        return ResponseEntity.ok(allApplication.stream()
                .collect(
                        ArrayList::new,
                        (arrayList, applicationDTO) -> arrayList.add(mapToApplicationResponse(applicationDTO)),
                        ArrayList::addAll ));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("applicationId") final UUID applicationId
    ) throws InvalidInputException {
        final ApplicationDTO applicationDTO = applicationservice.getApplication(applicationId);
        log.info("Found application {}", applicationDTO.getApplicationId());
        return ResponseEntity.ok(mapToApplicationResponse(applicationDTO));

    }

    @PostMapping("/applications/{applicationId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("applicationId") final UUID applicationId
    ) throws InvalidInputException {
        final ApplicationDTO applicationDTO = applicationservice.rejectApplication(applicationId);
        log.info("Application was rejected {}", applicationDTO.getApplicationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApplicationResponse(applicationDTO));
    }

    @PostMapping("/applications/{applicationId}/accept")
    public ResponseEntity<ApplicationResponse> acceptApplication(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("applicationId") final UUID applicationId
    ) throws InvalidInputException {
        final ApplicationDTO applicationDTO = applicationservice.acceptApplication(applicationId);
        log.info("Application was accepted {}", applicationDTO.getApplicationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApplicationResponse(applicationDTO));
    }

    @GetMapping("/events/{eventId}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByEvent(
            @PathVariable("organizerId") final UUID organizerId,
            @PathVariable("eventId") final UUID eventId) {
        final List<ApplicationDTO> applications = applicationservice.getAllApplicationsByOrganizerAndEventId(organizerId,eventId);
        return ResponseEntity.ok(applications.stream()
                .map(ResponseMapper::mapToApplicationResponse)
                .toList());
    }
}


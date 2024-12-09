package org.venow.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.Applicationservice;
import org.venow.backend.applicationservice.Eventservice;
import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.venow.backend.web.reponse.ResponseMapper.mapToApplicationResponse;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
public class OrganizerApplicationController {


    private final Applicationservice applicationservice;

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getallApplicationsByOrganizer(
            @PathVariable("organizerId") final UUID organizerId) {
        final List<ApplicationDTO> allApplication = applicationservice.getAllApplicationsByOrganizer(organizerId);
        log.info("Found application {}", allApplication.stream().map(ApplicationDTO::applicationId).toList());
        return ResponseEntity.ok(allApplication.stream()
                .collect(
                        ArrayList::new,
                        (arrayList, applicationDTO) -> arrayList.add( mapToApplicationResponse(applicationDTO)),
                        ArrayList::addAll ));
    }

}


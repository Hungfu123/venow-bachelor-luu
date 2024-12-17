package org.venow.backend.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.web.reponse.ContractResponse;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
public class OrganizerContractController {

    private final ApplicationService applicationService;
    private final Eventservice eventservice;
    private final UserService userService;

    @GetMapping("/contracts/{applicationId}")
    public ResponseEntity<ContractResponse> getContract(
            @PathVariable final UUID organizerId,
            @PathVariable final UUID applicationId
    ) {
        ApplicationDTO application = applicationService.getApplication(applicationId);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        String applicantName = applicationService.getApplicationName(applicationId);
        EventDTO event = eventservice.getEvent(application.getEventId());
        UserDTO organizer = userService.getUserByUserId(organizerId);

        ContractResponse contractResponse = new ContractResponse(
                UUID.randomUUID(),
                organizerId,
                applicantName,
                organizer.name(),
                event.title(),
                event.location(),
                event.startDate(),
                event.endDate(),
                application.getTypeOfBusiness(),
                application.getContactInformation(),
                application.getBusinessName(),
                "By accepting this contract, you agree to the terms and conditions of the event.",
                null,
                application.getStatus(),
                organizer.email()
        );


        return ResponseEntity.ok(contractResponse);
    }
}

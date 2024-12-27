package org.venow.backend.web;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.ContractService;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.InvalidStatusException;
import org.venow.backend.web.reponse.ContractResponse;
import org.venow.backend.web.reponse.ResponseMapper;
import org.venow.backend.web.request.ContractRequest;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/organizer/{organizerId}")
public class OrganizerContractController {

    private final ApplicationService applicationService;
    private final Eventservice eventservice;
    private final UserService userService;
    private final ContractService contractService;

    @GetMapping("/contracts/{applicationId}")
    public ResponseEntity<ContractResponse> getContractFormular(
            @PathVariable final UUID organizerId,
            @PathVariable final UUID applicationId
    ) {
        final ApplicationDTO application = applicationService.getApplication(applicationId);
        final String applicantName = applicationService.getApplicationName(applicationId);
        final EventDTO event = eventservice.getEvent(application.getEventId());
        final UserDTO organizer = userService.getUserByUserId(organizerId);
        final ContractResponse contractResponse = new ContractResponse(
                UUID.randomUUID(),
                organizerId,
                application.getUserId(),
                applicationId,
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
                null,
                application.getStatus(),
                organizer.email()
        );


        return ResponseEntity.ok(contractResponse);
    }

    @PostMapping("/contracts/{applicationId}")
    public ResponseEntity<ContractResponse> createContract(
            @PathVariable final UUID organizerId,
            @PathVariable final UUID applicationId,
            @Valid @RequestBody final ContractRequest contractRequest
    ) throws InvalidStatusException {
        final ContractDTO contract = contractService.createContract(applicationId, organizerId, contractRequest.price(), contractRequest.boothNumber());
        log.info("Contract was created {}", contract.getContractId());

        final ContractResponse body = ResponseMapper.mapToContractResponse(contract);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/contracts")
    public ResponseEntity<List<ContractResponse>> showAllContractsByOrganizer(
            @PathVariable("organizerId") final UUID organizerId){
        final List<ContractDTO> allContracts = contractService.getAllContractsByOrganizer(organizerId);

        final List<ContractResponse> body = allContracts.stream().map(ResponseMapper::mapToContractResponse).toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/contracts/details/{contractId}")
    public ResponseEntity<ContractResponse> getContractDetailsByOrganizer(
            @PathVariable final UUID organizerId,
            @PathVariable final UUID contractId
    ) {
        final ContractDTO contract = contractService.getContractByContractId(contractId);

        ContractResponse contractResponse = ResponseMapper.mapToContractResponse(contract);
        return ResponseEntity.ok(contractResponse);
    }



}

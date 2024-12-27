package org.venow.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.dto.*;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.exception.InvalidStatusException;
import org.venow.backend.service.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ContractService {
    private final ApplicationService applicationService;
    private final Eventservice eventservice;
    private final UserService userService;
    private final ContractRepository contractRepository;


    public ContractDTO createContract(final UUID applicationId, final UUID organizerId, PriceDto priceDto, String bootNumber) throws InvalidStatusException {
        if(applicationId == null || organizerId == null){
            throw new InvalidInputException("Application ID and Organizer ID must not be null");
        }

        final ApplicationDTO application = applicationService.getApplication(applicationId);
        if(!application.getStatus().equals(Status.IN_PROGRESS.name())) {
            throw new InvalidStatusException("Application Status must be in progress");
        }

        final EventDTO event = eventservice.getEvent(application.getEventId());
        final UserDTO organizer = userService.getUserByUserId(organizerId);
        final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());

        final String applicationName = applicationService.getApplicationName(applicationId);
        final ContractDTO contractDTO = ContractDTO.builder()
                .contractId(UUID.randomUUID())
                .application(application)
                .organizer(organizer)
                .exhibitor(exhibitor)
                .terms("By accepting this contract, you agree to the terms and conditions of the event.")
                .price(priceDto)
                .boothNumber(bootNumber)
                .status(Status.CONTRACT.name())
                .organizerEmail(organizer.email())
                .build();

        final ContractDTO savedContract = contractRepository.save(contractDTO);
        log.info("Contract created: {}", savedContract);

        return getContractDTO(savedContract, organizer, exhibitor, application, event, applicationName, contractDTO.getTerms(), contractDTO.getStatus(), contractDTO.getPrice());
    }


    public List<ContractDTO> getAllContractsByOrganizer(UUID organizerId) {
        if(!userService.existsByOrganizerId(organizerId)){
            throw new NotFoundException("Organizer ID not found");
        }
        List<ContractDTO> allByOrganizerId = contractRepository.findAllByOrganizerId(organizerId);

        return allByOrganizerId.stream().map(contract -> {
            final UserDTO organizer = userService.getUserByUserId(organizerId);

            final ApplicationDTO application = contract.getApplication();
            final EventDTO event = eventservice.getEvent(application.getEventId());

            final String applicationName = applicationService.getApplicationName(application.getApplicationId());
            final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());
            return getContractDTO(contract, organizer, exhibitor, application, event, applicationName, contract.getTerms(), contract.getStatus(), contract.getPrice());
        }).toList();
    }

    private ContractDTO getContractDTO(ContractDTO contract, UserDTO organizer, UserDTO exhibitor, ApplicationDTO application, EventDTO event, String applicationName, String terms, String status, PriceDto price) {
        return ContractDTO.builder()
                .contractId(contract.getContractId())
                .organizer(organizer)
                .exhibitor(exhibitor)
                .application(application)
                .applicantName(applicationName)
                .organizerName(organizer.name())
                .eventName(event.title())
                .location(event.location())
                .startDate(event.startDate().toString())
                .endDate(event.endDate().toString())
                .terms(terms)
                .status(status)
                .price(price)
                .boothNumber(contract.getBoothNumber())
                .organizerEmail(organizer.email())
                .build();
    }

    public ContractDTO getContractByContractId(UUID contractId) {
        return contractRepository.findById(contractId)
                .map(contract -> {
                    final UserDTO organizer = userService.getUserByUserId(contract.getOrganizer().userId());

                    final ApplicationDTO application = contract.getApplication();
                    final EventDTO event = eventservice.getEvent(application.getEventId());

                    final String applicationName = applicationService.getApplicationName(application.getApplicationId());
                    final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());

                    return getContractDTO(contract, organizer, exhibitor, application, event, applicationName, contract.getTerms(), contract.getStatus(), contract.getPrice());
                })
                .orElseThrow(() -> new NotFoundException("Contract ID not found"));
    }



    public ContractDTO getContractByApplicationIdAndContactId(UUID applicationId, UUID contractId) {
        if (applicationId == null) {
            throw new InvalidInputException("Application ID must not be null");
        }

        ContractDTO contractDTO = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("Contract not found for ID: " + contractId));
        final UserDTO organizer = userService.getUserByUserId(contractDTO.getOrganizer().userId());
        final ApplicationDTO application = applicationService.getApplication(applicationId);
        final EventDTO event = eventservice.getEvent(application.getEventId());
        final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());

        return getContractDTO(contractDTO, organizer, exhibitor, application, event, exhibitor.name(), contractDTO.getTerms(), contractDTO.getStatus(), contractDTO.getPrice());
    }

}

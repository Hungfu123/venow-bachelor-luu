package org.venow.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Role;
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

    @PersistenceContext
    private EntityManager entityManager;

    public ContractDTO createContract(final UUID applicationId, final UUID organizerId, PriceDto priceDto, String bootNumber) throws InvalidStatusException {
        if (applicationId == null || organizerId == null) {
            throw new InvalidInputException("Application ID and Organizer ID must not be null");
        }

        final ApplicationDTO application = applicationService.getApplication(applicationId);
        if (!application.getStatus().equals(Status.IN_PROGRESS.name())) {
            throw new InvalidStatusException("Application Status must be in progress");
        }

        final EventDTO event = eventservice.getEvent(application.getEventId());
        final UserDTO organizer = userService.getUserByUserId(organizerId);
        final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());

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

        return buildContractDTOTogether(savedContract, organizer, exhibitor, application, event);
    }


    public List<ContractDTO> getAllContractsByOrganizer(UUID organizerId) {
        if (!userService.existsByUserAndRole(organizerId, Role.ORGANIZER)) {
            throw new NotFoundException("Organizer ID not found");
        }
        List<ContractDTO> allByOrganizerId = contractRepository.findAllByOrganizerId(organizerId);

        return allByOrganizerId.stream().map(contract -> {
            final UserDTO organizer = userService.getUserByUserId(organizerId);

            final ApplicationDTO application = contract.getApplication();
            final EventDTO event = eventservice.getEvent(application.getEventId());

            final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());
            return buildContractDTOTogether(contract, organizer, exhibitor, application, event);
        }).toList();
    }

    private ContractDTO buildContractDTOTogether(ContractDTO contract, UserDTO organizer, UserDTO exhibitor, ApplicationDTO application, EventDTO event) {
        return ContractDTO.builder()
                .contractId(contract.getContractId())
                .organizer(organizer)
                .exhibitor(exhibitor)
                .application(application)
                .applicantName(exhibitor.name())
                .organizerName(organizer.name())
                .eventName(event.title())
                .location(event.location())
                .startDate(event.startDate().toString())
                .endDate(event.endDate().toString())
                .terms(contract.getTerms())
                .status(contract.getStatus())
                .price(contract.getPrice())
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

                    final UserDTO exhibitor = userService.getUserByUserId(application.getUserId());

                    return buildContractDTOTogether(contract, organizer, exhibitor, application, event);
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

        return buildContractDTOTogether(contractDTO, organizer, exhibitor, application, event);
    }

    public void updateContractStatus(UUID contractId, Status status) throws InvalidStatusException {
        final ContractDTO contractDTO = contractRepository.findById(contractId).orElseThrow(() -> new NotFoundException("Contract not found for ID: " + contractId));
        if (!contractDTO.getStatus().equals(Status.CONTRACT.name())) {
            throw new InvalidStatusException("Contract status must be CONTRACT");
        }
        contractDTO.setStatus(status.name());
        contractRepository.save(contractDTO);

        log.info("Contract status updated: {} with {}", contractId, status);
    }

    public List<ContractDTO> getAllContractsByExhibitor(UUID exhibitorId) {
        if (!userService.existsByUserAndRole(exhibitorId, Role.EXHIBITOR)) {
            throw new NotFoundException("Exhibitor ID not found");
        }
        List<ContractDTO> allByExhibitorId = contractRepository.findAllByExhibitorId(exhibitorId);

        return allByExhibitorId.stream().map(contract -> {
            final UserDTO exhibitor = userService.getUserByUserId(exhibitorId);

            final ApplicationDTO application = contract.getApplication();
            final EventDTO event = eventservice.getEvent(application.getEventId());

            final UserDTO organizer = userService.getUserByUserId(event.organizerId());
            return buildContractDTOTogether(contract, organizer, exhibitor, application, event);
        }).toList();
    }
}

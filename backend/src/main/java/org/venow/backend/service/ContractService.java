package org.venow.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Status;
import org.venow.backend.infrastructure.ContractRepositoryImpl;
import org.venow.backend.service.dto.*;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.mapper.ContractMapper;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ContractService {
    private final ApplicationService applicationService;
    private final Eventservice eventservice;
    private final UserService userService;
    private final ContractRepositoryImpl contractRepository;


    public ContractDTO createContract(final UUID applicationId, final UUID organizerId, PriceDto priceDto, String bootNumber) {
        if(applicationId == null || organizerId == null){
            throw new InvalidInputException("Application ID and Organizer ID must not be null");
        }

        final ApplicationDTO application = applicationService.getApplication(applicationId);
        final EventDTO event = eventservice.getEvent(application.getEventId());
        final UserDTO organizer = userService.getUserByUserId(organizerId);

        final String applicationName = applicationService.getApplicationName(applicationId);
        final ContractDTO contractDTO = ContractDTO.builder()
                .contractId(UUID.randomUUID())
                .organizerId(organizerId)
                .terms("By accepting this contract, you agree to the terms and conditions of the event.")
                .price(priceDto)
                .boothNumber(bootNumber)
                .status(Status.CONTRACT.name())
                .organizerEmail(organizer.email())
                .build();

        final ContractDTO savedContract = contractRepository.save(contractDTO);
        log.info("Contract created: {}", savedContract);
        final ContractDTO savedContractDTO = ContractDTO.builder()
                .contractId(savedContract.getContractId())
                .organizerId(organizerId)
                .applicantName(applicationName)
                .organizerName(organizer.name())
                .eventName(event.title())
                .location(event.location())
                .startDate(String.valueOf(event.startDate()))
                .endDate(String.valueOf(event.endDate()))
                .typeOfBusiness(application.getTypeOfBusiness())
                .contactInformation(application.getContactInformation())
                .businessName(application.getBusinessName())
                .terms(contractDTO.getTerms())
                .status(contractDTO.getStatus())
                .price(contractDTO.getPrice())
                .boothNumber(savedContract.getBoothNumber())
                .organizerEmail(organizer.email())
                .build();

        return savedContractDTO;
    }


}

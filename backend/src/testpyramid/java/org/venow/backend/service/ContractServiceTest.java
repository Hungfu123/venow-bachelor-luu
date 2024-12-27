package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.dto.*;
import org.venow.backend.service.exception.ApplicationNotFoundException;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.exception.InvalidStatusException;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.ContractDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private Eventservice eventservice;
    @Mock
    private UserService userService;

    @InjectMocks
    private ContractService contractService;

    @Test
    @DisplayName("should create contract")
    void shouldCreateContract() throws InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final EventDTO event = EventDTOFactory.createDefault().build();
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final PriceDto price = new PriceDto(10000);
        final ContractDTO contract = ContractDTOFactory.createDefault().withPrice(10000).build();

        when(eventservice.getEvent(application.getEventId())).thenReturn(event);
        when(userService.getUserByUserId(any())).thenReturn(organizer);
        when(applicationService.getApplication(application.getApplicationId())).thenReturn(application);
        when(applicationService.getApplicationName(application.getApplicationId())).thenReturn("applicantName");
        when(contractRepository.save(any())).thenReturn(contract);

        ContractDTO createdContract = contractService.createContract(application.getApplicationId(), organizer.userId(), price, "B99");

        assertThat(createdContract.getContractId()).isEqualTo(contract.getContractId());
        assertThat(createdContract.getOrganizer().userId()).isEqualTo(organizer.userId());
        assertThat(createdContract.getApplication()).isEqualTo(application);
        assertThat(createdContract.getApplicantName()).isEqualTo("applicantName");
        assertThat(createdContract.getOrganizerName()).isEqualTo(organizer.name());
        assertThat(createdContract.getEventName()).isEqualTo(event.title());
        assertThat(createdContract.getLocation()).isEqualTo(event.location());
        assertThat(createdContract.getTerms()).isEqualTo("By accepting this contract, you agree to the terms and conditions of the event.");
        assertThat(createdContract.getPrice().getAmountInEuro()).isEqualTo(contract.getPrice().getAmountInEuro());
        assertThat(createdContract.getBoothNumber()).isEqualTo(contract.getBoothNumber());
        assertThat(createdContract.getStatus()).isEqualTo(contract.getStatus());
        assertThat(createdContract.getOrganizerEmail()).isEqualTo(organizer.email());
    }

    @Test
    @DisplayName("should throw exception when applicationId or organizerId is null")
    void shouldThrowExceptionWhenApplicationIdOrOrganizerIdIsNull() {
        assertThatThrownBy(() -> contractService.createContract(null, UUID.randomUUID(), new PriceDto(10000), "B99"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Application ID and Organizer ID must not be null");

        assertThatThrownBy(() -> contractService.createContract(UUID.randomUUID(), null, new PriceDto(10000), "B99"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Application ID and Organizer ID must not be null");

        assertThatThrownBy(() -> contractService.createContract(null, null, new PriceDto(10000), "B99"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Application ID and Organizer ID must not be null");
    }

    @Test
    @DisplayName("should throw exception when application not found")
    void shouldThrowExceptionWhenApplicationNotFound() {
        when(applicationService.getApplication(any())).thenThrow(new ApplicationNotFoundException("Application not found"));

        assertThatThrownBy(() -> contractService.createContract(UUID.randomUUID(), UUID.randomUUID(), new PriceDto(10000), "B99"))
                .isInstanceOf(ApplicationNotFoundException.class)
                .hasMessage("Application not found");
    }

    @Test
    @DisplayName("should throw exception when event not found")
    void shouldThrowExceptionWhenEventNotFound() {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();

        when(applicationService.getApplication(application.getApplicationId())).thenReturn(application);
        when(eventservice.getEvent(application.getEventId())).thenThrow(new InvalidInputException("Event not found"));

        assertThatThrownBy(() -> contractService.createContract(application.getApplicationId(), UUID.randomUUID(), new PriceDto(10000), "B99"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Event not found");
    }

    @Test
    @DisplayName("should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();

        when(applicationService.getApplication(application.getApplicationId())).thenReturn(application);
        when(eventservice.getEvent(application.getEventId())).thenReturn(EventDTOFactory.createDefault().build());
        when(userService.getUserByUserId(any())).thenThrow(new InvalidInputException("User not found"));

        assertThatThrownBy(() -> contractService.createContract(application.getApplicationId(), UUID.randomUUID(), new PriceDto(10000), "B99"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("should create contract with minimal valid values")
    void shouldCreateContractWithMinimalValidValues() throws InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final EventDTO event = EventDTOFactory.createDefault().build();
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final PriceDto price = new PriceDto(0);
        final ContractDTO contract = ContractDTOFactory.createDefault().withPrice(1).build();

        when(applicationService.getApplication(application.getApplicationId())).thenReturn(application);
        when(eventservice.getEvent(application.getEventId())).thenReturn(event);
        when(userService.getUserByUserId(any())).thenReturn(organizer);
        when(contractRepository.save(any())).thenReturn(contract);

        ContractDTO createdContract = contractService.createContract(application.getApplicationId(), organizer.userId(), price, "A50");

        assertThat(createdContract.getPrice().getAmountInEuro()).isEqualByComparingTo(BigDecimal.valueOf(0));
        assertThat(createdContract.getBoothNumber()).isEqualTo("A50");
    }

    @Test
    @DisplayName("should throw InvalidStatus")
    void shouldthrowInvalidStatus() throws InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().withStatus(Status.CONTRACT.name()).build();
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final PriceDto price = new PriceDto(0);
        when(applicationService.getApplication(application.getApplicationId())).thenReturn(application);

        assertThatThrownBy(() -> contractService.createContract(application.getApplicationId(), organizer.userId(), price, "A50"))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("Application Status must be in progress");
    }

    @Test
    @DisplayName("Organizer sees all their contracts")
    void shouldReturnAllContractsForOrganizer() {
        final UUID organizerId = UUID.randomUUID();
        final EventDTO event1 = EventDTOFactory.createDefault().withOrganizerId(organizerId).build();
        final EventDTO event2 = EventDTOFactory.createDefault().withOrganizerId(organizerId).build();
        final ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId(event1.eventId().toString())
                .build();

        final ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withEventId(event1.eventId().toString())
                .build();

        final ApplicationDTO application3 = ApplicationDTOFactory.createDefault()
                .withEventId(event2.eventId().toString())
                .build();

        final UserDTO organizer = UserDTOFactory.createDefault().withUserId(organizerId).build();

        final ContractDTO contract1 = ContractDTOFactory.createDefault()
                .withOrganizer(organizer)
                .withApplication(application1)
                .withContractId(UUID.randomUUID().toString())
                .build();

        final ContractDTO contract2 = ContractDTOFactory.createDefault()
                .withOrganizer(organizer)
                .withApplication(application2)
                .withContractId(UUID.randomUUID().toString())
                .build();


        final ContractDTO contract3 = ContractDTOFactory.createDefault()
                .withOrganizer(organizer)
                .withApplication(application3)
                .withContractId(UUID.randomUUID().toString())
                .build();

        when(contractRepository.findAllByOrganizerId(organizerId)).thenReturn(List.of(contract1, contract2, contract3));
        when(userService.getUserByUserId(organizerId)).thenReturn(organizer);
        when(eventservice.getEvent(application1.getEventId())).thenReturn(event1);
        when(eventservice.getEvent(application2.getEventId())).thenReturn(event1);
        when(eventservice.getEvent(application3.getEventId())).thenReturn(event2);
        when(applicationService.getApplicationName(application1.getApplicationId())).thenReturn("Test Application");
        when(applicationService.getApplicationName(application2.getApplicationId())).thenReturn("Test Application");
        when(applicationService.getApplicationName(application3.getApplicationId())).thenReturn("Test Application");
        when(userService.existsByOrganizerId(organizerId)).thenReturn(true);

        List<ContractDTO> contracts = contractService.getAllContractsByOrganizer(organizerId);

        assertThat(contracts).isNotEmpty();
        assertThat(contracts).hasSize(3);
        assertThat(contracts.stream().map(contract -> contract.getOrganizer().userId())).allMatch(id -> id.equals(organizerId));
        verify(contractRepository).findAllByOrganizerId(organizerId);
        verify(userService, times(3)).getUserByUserId(organizerId);
    }

    @Test
    @DisplayName("Empty contract list for organizer with no contracts")
    void shouldReturnEmptyListWhenNoContractsFoundForOrganizer() {
        final UUID organizerId = UUID.randomUUID();
        when(contractRepository.findAllByOrganizerId(organizerId)).thenReturn(Collections.emptyList());
        when(userService.existsByOrganizerId(organizerId)).thenReturn(true);
        List<ContractDTO> contracts = contractService.getAllContractsByOrganizer(organizerId);

        assertThat(contracts).isEmpty();
        verify(contractRepository).findAllByOrganizerId(organizerId);
    }

    @Test
    @DisplayName("Throws exception when organizer not found")
    void shouldThrowExceptionWhenOrganizerNotFound() {
        final UUID organizerId = UUID.randomUUID();
        when(userService.existsByOrganizerId(organizerId)).thenThrow(new NotFoundException("Organizer not found"));

        assertThatThrownBy(() -> contractService.getAllContractsByOrganizer(organizerId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organizer not found");
        verifyNoInteractions(contractRepository);
    }

    @Test
    @DisplayName("should return contract by contractId")
    void shouldReturnContractByContractId() {
        final UUID contractId = UUID.randomUUID();
        final ContractDTO contract = ContractDTOFactory.createDefault().withContractId(contractId.toString()).build();
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        EventDTO event = EventDTOFactory.createDefault().build();
        UserDTO user = UserDTOFactory.createDefault().build();
        when(userService.getUserByUserId(contract.getOrganizer().userId())).thenReturn(user);
        when(eventservice.getEvent(any())).thenReturn(event);
        when(applicationService.getApplicationName(any())).thenReturn("Default Applicant");

        ContractDTO foundContract = contractService.getContractByContractId(contractId);

        verify(contractRepository).findById(contractId);
        assertThat(foundContract.getContractId()).isEqualTo(contract.getContractId());
        assertThat(foundContract.getOrganizer().userId()).isEqualTo(user.userId());
        assertThat(foundContract.getApplication()).isEqualTo(contract.getApplication());
        assertThat(foundContract.getApplicantName()).isEqualTo(contract.getApplicantName());
        assertThat(foundContract.getOrganizerName()).isEqualTo(user.name());
        assertThat(foundContract.getEventName()).isEqualTo(event.title());
        assertThat(foundContract.getLocation()).isEqualTo(event.location());
        assertThat(foundContract.getTerms()).isEqualTo(contract.getTerms());
        assertThat(foundContract.getPrice()).isEqualTo(contract.getPrice());
        assertThat(foundContract.getBoothNumber()).isEqualTo(contract.getBoothNumber());
        assertThat(foundContract.getStatus()).isEqualTo(contract.getStatus());
        assertThat(foundContract.getOrganizerEmail()).isEqualTo(user.email());
    }

    @Test
    @DisplayName("should throw exception when contract not found")
    void shouldThrowExceptionWhenContractNotFound() {
        final UUID contractId = UUID.randomUUID();
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.getContractByContractId(contractId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Contract ID not found");
    }



}

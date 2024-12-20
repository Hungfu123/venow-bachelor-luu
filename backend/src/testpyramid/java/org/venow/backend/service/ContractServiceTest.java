package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.service.dto.*;
import org.venow.backend.service.exception.ApplicationNotFoundException;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.ContractDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void shouldCreateContract() {
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
        assertThat(createdContract.getOrganizerId()).isEqualTo(organizer.userId());
        assertThat(createdContract.getApplicantName()).isEqualTo("applicantName");
        assertThat(createdContract.getOrganizerName()).isEqualTo(organizer.name());
        assertThat(createdContract.getEventName()).isEqualTo(event.title());
        assertThat(createdContract.getLocation()).isEqualTo(event.location());
        assertThat(createdContract.getTypeOfBusiness()).isEqualTo(application.getTypeOfBusiness());
        assertThat(createdContract.getContactInformation()).isEqualTo(application.getContactInformation());
        assertThat(createdContract.getBusinessName()).isEqualTo(application.getBusinessName());
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
    void shouldCreateContractWithMinimalValidValues() {
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




}

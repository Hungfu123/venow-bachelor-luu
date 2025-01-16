package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.AlreadyRejectedException;
import org.venow.backend.service.exception.DuplicateApplicationException;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.domain.model.Status;
import org.venow.backend.infrastructure.ApplicationRepositoryImpl;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ApplicationService service;

    @Test
    @DisplayName("should throw exception when exhibitor already applied for the event")
    void shouldThrowExceptionWhenCreateApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(applicationDTO.getUserId(), applicationDTO.getEventId()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.createApplication(applicationDTO))
                .isInstanceOf(DuplicateApplicationException.class)
                .hasMessageContaining("User has already applied for this event");
    }

    @Test
    @DisplayName("should create a new application successfully when no duplicate exists")
    void shouldCreateApplicationSuccessfully() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        assertThat(service.createApplication(applicationDTO)).isEqualTo(applicationDTO);
    }
    @Test
    @DisplayName("Should throw exception when eventId is null")
    void shouldThrowExceptionWhenEventIdIsNull() {
        final ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .id(1L)
                .applicationId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .eventId(null)
                .businessName("Luu Business")
                .typeOfBusiness("Luu Type")
                .previousParticipation(false)
                .message("Nimm mich")
                .status("IN_PROGRESS")
                .build();

        assertThatThrownBy(() -> service.createApplication(applicationDTO))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("User ID and Event ID must not be null");
    }



    @Test
    @DisplayName("Should set initial status to IN_PROGRESS")
    void shouldSetInitialStatus() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        final ApplicationDTO result = service.createApplication(applicationDTO);

        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @DisplayName("Should allow user to apply for multiple different events")
    void shouldAllowApplicationForDifferentEvents() {

        final UserDTO user = UserDTOFactory.createDefault().build();
        final EventDTO event1 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle("Event 1").build();
        final EventDTO event2 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle("Event2").build();

        final ApplicationDTO applicationDTO1 = ApplicationDTO.builder().userId(user.userId()).eventId(event1.eventId()).build();
        final ApplicationDTO applicationDTO2 = ApplicationDTO.builder().userId(user.userId()).eventId(event2.eventId()).build();

        when(applicationRepository.existsByUserIdAndEventId(user.userId(), event1.eventId())).thenReturn(false);
        when(applicationRepository.existsByUserIdAndEventId(user.userId(), event2.eventId())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO1).thenReturn(applicationDTO2);
        when(userService.getUserByUserId(user.userId())).thenReturn(user);
        final ApplicationDTO result1 = service.createApplication(applicationDTO1);
        final ApplicationDTO result2 = service.createApplication(applicationDTO2);

        assertThat(result1.getEventId()).isEqualTo(event1.eventId());
        assertThat(result2.getEventId()).isEqualTo(event2.eventId());
    }

    @Test
    @DisplayName("organizer gets all applications")
    void organizerGetsAllApplications() {
        final ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        final ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        final List<ApplicationDTO> expectedApplications = List.of(application1, application2);

        when(applicationRepository.getAllApplicationsByOrganizer(any()))
                .thenReturn(expectedApplications);
        when(userService.getUserByUserId(any()))
                .thenReturn(UserDTOFactory.createDefault().build());
        when(applicationRepository.getEventName(any()))
                .thenReturn("Event");
        final List<ApplicationDTO> result = service.getAllApplicationsByOrganizer(any());

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(application1, application2);
    }

    @Test
    @DisplayName("Should return empty list when organizer has no applications")
    void shouldReturnEmptyListWhenNoApplications() {
        final UUID organizerId = UUID.randomUUID();

        when(applicationRepository.getAllApplicationsByOrganizer(organizerId))
                .thenReturn(List.of());

        final List<ApplicationDTO> result = service.getAllApplicationsByOrganizer(organizerId);

        assertThat(result)
                .isEmpty();
    }

    @Test
    @DisplayName("organizer gets application")
    void organizerGetsApplications() throws InvalidInputException {
        final ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        when(applicationRepository.findById(any()))
                .thenReturn(Optional.ofNullable(application1));
        when(userService.getUserByUserId(any()))
                .thenReturn(UserDTOFactory.createDefault().build());

        assert application1 != null;
        final ApplicationDTO application = service.getApplication(application1.getApplicationId());

        assertThat(application).isEqualTo(application1);
    }

    @Test
    @DisplayName("organizer gets Error getting application")
    void organizerGetsError() {

        assertThatThrownBy(() -> service.getApplication(null))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    @DisplayName("organizer gets Error when application is not found")
    void organizerGetsErrorForNonExistentApplication() {
        final UUID nonExistentId = UUID.randomUUID();

        when(applicationRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplication(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Application not found");
    }

    @Test
    @DisplayName("organizer reject application")
    void organizerRejectApplication() {

        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        when(applicationRepository.save(any())).thenReturn(applicationDTO);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        final ApplicationDTO app = service.rejectApplication(applicationDTO1.get().getApplicationId());

        assertThat(app.getStatus()).isEqualTo("REJECTED");
    }
    @Test
    @DisplayName("organizer reject already rejected application")
    void organizerRejectAlreadyRejectedApplication() {

        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("REJECTED");
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());

        assertThatThrownBy(() -> service.rejectApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already rejected");
    }

    @Test
    @DisplayName("organizer accept application")
    void organizerAcceptApplication() {

        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        when(applicationRepository.save(any())).thenReturn(applicationDTO);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        final ApplicationDTO app = service.acceptApplication(applicationDTO1.get().getApplicationId());

        assertThat(app.getStatus()).isEqualTo(Status.ACCEPTED.toString());
    }

    @Test
    @DisplayName("organizer accept already accepted application")
    void organizerAcceptAlreadyAcceptedApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("ACCEPTED");
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("organizer accept already accepted application")
    void organizerAcceptAlreadyRejectedApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("REJECTED");
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("organizer reject already accepted application")
    void organizerRejectAlreadyAcceptedApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("ACCEPTED");
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("get application name from user")
    void getApplicationNameFromUser() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        final UserDTO user = UserDTOFactory.createDefault().build();

        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(applicationDTO1.get().getUserId())).thenReturn(user);
        final String applicationName = service.getApplicationName(applicationDTO1.get().getApplicationId());

        assertThat(applicationName).isEqualTo(user.name());
    }

    @Test
    @DisplayName("Should return all applications for a given organizer and event")
    void shouldReturnAllApplicationsForOrganizerAndEvent() {
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        List<ApplicationDTO> expectedApplications = List.of(
                ApplicationDTO.builder()
                        .id(1L)
                        .applicationId(UUID.randomUUID())
                        .userId(UUID.randomUUID())
                        .eventId(eventId)
                        .businessName("Business 1")
                        .typeOfBusiness("Type 1")
                        .previousParticipation(true)
                        .message("Message 1")
                        .status("IN_PROGRESS")
                        .build(),
                ApplicationDTO.builder()
                        .id(2L)
                        .applicationId(UUID.randomUUID())
                        .userId(UUID.randomUUID())
                        .eventId(eventId)
                        .businessName("Business 2")
                        .typeOfBusiness("Type 2")
                        .previousParticipation(false)
                        .message("Message 2")
                        .status("IN_PROGRESS")
                        .build()
        );

        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, eventId))
                .thenReturn(expectedApplications);
        when(userService.getUserByUserId(any()))
                .thenReturn(UserDTO.builder().userId(UUID.randomUUID()).name("User 1").build())
                .thenReturn(UserDTO.builder().userId(UUID.randomUUID()).name("User 2").build());
        List<ApplicationDTO> result = service.getAllApplicationsByOrganizerAndEventId(organizerId, eventId);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(expectedApplications);
    }

    @Test
    @DisplayName("Should return empty list when no applications are found for organizer and event")
    void shouldReturnEmptyListWhenNoApplicationsFound() {
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, eventId))
                .thenReturn(List.of());

        List<ApplicationDTO> result = service.getAllApplicationsByOrganizerAndEventId(organizerId, eventId);

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when organizer ID is null")
    void shouldThrowExceptionWhenOrganizerIdIsNull() {
        UUID eventId = UUID.randomUUID();
        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(null, eventId))
                .thenThrow(new IllegalArgumentException("Organizer ID and Event ID must not be null"));
        assertThatThrownBy(() -> service.getAllApplicationsByOrganizerAndEventId(null, eventId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Organizer ID and Event ID must not be null");
    }

    @Test
    @DisplayName("Should throw exception when event ID is null")
    void shouldThrowExceptionWhenEventIdIsNull2() {
        UUID organizerId = UUID.randomUUID();
        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, null))
                .thenThrow(new IllegalArgumentException("Organizer ID and Event ID must not be null"));
        assertThatThrownBy(() -> service.getAllApplicationsByOrganizerAndEventId(organizerId, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Organizer ID and Event ID must not be null");
    }

    @Test
    @DisplayName("Should throw exception when event ID is null")
    void shouldThrowExceptionWhenEventIdIsNull3() {
        UUID organizerId = UUID.randomUUID();
        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, null))
                .thenThrow(new IllegalArgumentException("Organizer ID and Event ID must not be null"));
        assertThatThrownBy(() -> service.getAllApplicationsByOrganizerAndEventId(organizerId, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Organizer ID and Event ID must not be null");
    }






}

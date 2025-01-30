package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.AlreadyRejectedException;
import org.venow.backend.service.exception.DuplicateApplicationException;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    @DisplayName("should create a new application successfully and set eventTitle and exhibitorName")
    void shouldCreateApplicationSuccessfully() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final ApplicationDTO savedApplication = ApplicationDTOFactory.createDefault().build();

        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().withName("Test User").build());
        when(applicationRepository.getEventName(any())).thenReturn("Test Event");
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationDTO result = service.createApplication(applicationDTO);

        ArgumentCaptor<ApplicationDTO> captor = ArgumentCaptor.forClass(ApplicationDTO.class);
        verify(applicationRepository).save(captor.capture());

        ApplicationDTO capturedApplication = captor.getValue();
        assertThat(capturedApplication.getEventTitle()).isEqualTo("Test Event");
        assertThat(capturedApplication.getExhibitorName()).isEqualTo("Test User");
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
    @DisplayName("should retrieve all applications by organizer and set eventTitle and exhibitorName")
    void organizerGetsAllApplications() {
        final UUID organizerId = UUID.randomUUID();
        final UUID eventId1 = UUID.randomUUID();
        final UUID eventId2 = UUID.randomUUID();
        final UUID userId1 = UUID.randomUUID();
        final UUID userId2 = UUID.randomUUID();

        final ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withApplicationId(UUID.randomUUID().toString())
                .withUserId(userId1.toString())
                .withEventId(eventId1.toString())
                .build();

        final ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withApplicationId(UUID.randomUUID().toString())
                .withUserId(userId2.toString())
                .withEventId(eventId2.toString())
                .build();

        final List<ApplicationDTO> mockApplications = List.of(application1, application2);

        when(applicationRepository.getAllApplicationsByOrganizer(organizerId)).thenReturn(mockApplications);
        when(userService.getUserByUserId(userId1)).thenReturn(UserDTOFactory.createDefault().withName("User One").build());
        when(userService.getUserByUserId(userId2)).thenReturn(UserDTOFactory.createDefault().withName("User Two").build());
        when(applicationRepository.getEventName(eventId1)).thenReturn("Event One");
        when(applicationRepository.getEventName(eventId2)).thenReturn("Event Two");

        final List<ApplicationDTO> result = service.getAllApplicationsByOrganizer(organizerId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEventTitle()).isEqualTo("Event One");
        assertThat(result.get(0).getExhibitorName()).isEqualTo("User One");
        assertThat(result.get(1).getEventTitle()).isEqualTo("Event Two");
        assertThat(result.get(1).getExhibitorName()).isEqualTo("User Two");

        final ArgumentCaptor<ApplicationDTO> captor = ArgumentCaptor.forClass(ApplicationDTO.class);
        verify(applicationRepository).getAllApplicationsByOrganizer(organizerId);
        verify(userService).getUserByUserId(userId1);
        verify(userService).getUserByUserId(userId2);
        verify(applicationRepository).getEventName(eventId1);
        verify(applicationRepository).getEventName(eventId2);
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
    @DisplayName("should retrieve application by ID and set eventTitle and exhibitorName")
    void organizerGetsApplication() {
        UUID applicationId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withApplicationId(applicationId.toString())
                .withUserId(userId.toString())
                .withEventId(eventId.toString())
                .build();

        UserDTO user = UserDTOFactory.createDefault().withName("Test User").build();
        String eventName = "Test Event";

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userService.getUserByUserId(userId)).thenReturn(user);
        when(applicationRepository.getEventName(eventId)).thenReturn(eventName);

        ApplicationDTO result = service.getApplication(applicationId);

        assertThat(result).isNotNull();
        assertThat(result.getApplicationId()).isEqualTo(applicationId);
        assertThat(result.getEventTitle()).isEqualTo(eventName);
        assertThat(result.getExhibitorName()).isEqualTo("Test User");
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
    @DisplayName("should reject application and set eventTitle and exhibitorName")
    void organizerRejectApplication() {
        UUID applicationId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withApplicationId(applicationId.toString())
                .withUserId(userId.toString())
                .withEventId(eventId.toString())
                .withStatus(Status.IN_PROGRESS.name())
                .build();

        UserDTO user = UserDTOFactory.createDefault().withName("Reject User").build();
        String eventName = "Reject Event";

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(ApplicationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.getUserByUserId(userId)).thenReturn(user);
        when(applicationRepository.getEventName(eventId)).thenReturn(eventName);

        ApplicationDTO result = service.rejectApplication(applicationId);

        assertThat(result.getStatus()).isEqualTo(Status.REJECTED.name());
        assertThat(result.getEventTitle()).isEqualTo(eventName);
        assertThat(result.getExhibitorName()).isEqualTo("Reject User");
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
    @DisplayName("organizer accept application and set eventTitle and exhibitorName")
    void organizerAcceptApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final ApplicationDTO savedApplication = ApplicationDTOFactory.createDefault()
                .withStatus(Status.CONTRACT.toString())
                .withEventTitle("Accepted Event")
                .withExhibitorName("Accepted User")
                .build();
        final Optional<ApplicationDTO> applicationDTOOptional = Optional.of(applicationDTO);

        when(applicationRepository.findById(any())).thenReturn(applicationDTOOptional);
        when(applicationRepository.save(any())).thenReturn(savedApplication);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().withName("Accepted User").build());
        when(applicationRepository.getEventName(any())).thenReturn("Accepted Event");

        ApplicationDTO result = service.acceptApplication(applicationDTO.getApplicationId());

        assertThat(result.getStatus()).isEqualTo(Status.CONTRACT.toString());
        assertThat(result.getEventTitle()).isEqualTo("Accepted Event");
        assertThat(result.getExhibitorName()).isEqualTo("Accepted User");
    }


    @Test
    @DisplayName("organizer accept already accepted application")
    void organizerAcceptAlreadyAcceptedApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("CONTRACT");
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
        applicationDTO.setStatus("CONTRACT");
        final Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("should retrieve the exhibitor name for a given application ID")
    void getApplicationNameFromUser() {
        UUID applicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withApplicationId(applicationId.toString())
                .withUserId(userId.toString())
                .build();

        UserDTO user = UserDTOFactory.createDefault().withName("Exhibitor Name").build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userService.getUserByUserId(userId)).thenReturn(user);

        String exhibitorName = service.getApplicationName(applicationId);

        assertThat(exhibitorName).isEqualTo("Exhibitor Name");
    }


    @Test
    @DisplayName("should retrieve all applications by organizer and event ID and set eventTitle and exhibitorName")
    void shouldReturnAllApplicationsForOrganizerAndEvent() {
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withApplicationId(UUID.randomUUID().toString())
                .withUserId(userId1.toString())
                .withEventId(eventId.toString())
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withApplicationId(UUID.randomUUID().toString())
                .withUserId(userId2.toString())
                .withEventId(eventId.toString())
                .build();

        List<ApplicationDTO> mockApplications = List.of(application1, application2);

        when(applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, eventId)).thenReturn(mockApplications);
        when(userService.getUserByUserId(userId1)).thenReturn(UserDTOFactory.createDefault().withName("User One").build());
        when(userService.getUserByUserId(userId2)).thenReturn(UserDTOFactory.createDefault().withName("User Two").build());
        when(applicationRepository.getEventName(eventId)).thenReturn("Specific Event");

        List<ApplicationDTO> result = service.getAllApplicationsByOrganizerAndEventId(organizerId, eventId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEventTitle()).isEqualTo("Specific Event");
        assertThat(result.get(0).getExhibitorName()).isEqualTo("User One");
        assertThat(result.get(1).getEventTitle()).isEqualTo("Specific Event");
        assertThat(result.get(1).getExhibitorName()).isEqualTo("User Two");
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

    @Test
    @DisplayName("createApplication - should throw InvalidInputException if userId or eventId is null")
    void createApplicationWithNullShouldThrowException() {
        ApplicationDTO invalidAppDto = ApplicationDTOFactory.createDefault().build();
        invalidAppDto.setUserId(null);
        invalidAppDto.setEventId(null);

        assertThatThrownBy(() -> service.createApplication(invalidAppDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("User ID and Event ID must not be null");
    }

    @Test
    @DisplayName("should set eventTitle and exhibitorName when creating application")
    void shouldSetEventTitleAndExhibitorName() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        final ApplicationDTO savedApplication = ApplicationDTOFactory.createDefault().build();

        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(savedApplication);
        when(userService.getUserByUserId(any())).thenReturn(UserDTOFactory.createDefault().build());
        when(applicationRepository.getEventName(any())).thenReturn("Test");

        service.createApplication(applicationDTO);

        ArgumentCaptor<ApplicationDTO> applicationCaptor = ArgumentCaptor.forClass(ApplicationDTO.class);
        verify(applicationRepository).save(applicationCaptor.capture());

        ApplicationDTO capturedApplication = applicationCaptor.getValue();
        assertThat(capturedApplication.getEventTitle()).isEqualTo("Test");
        assertThat(capturedApplication.getExhibitorName()).isEqualTo("Testuser");
    }


    @Test
    @DisplayName("Should accept application and ensure eventTitle, exhibitorName, and status are set")
    void acceptApplicationShouldSetFields() {
        UUID applicationId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withApplicationId(applicationId.toString())
                .withUserId(userId.toString())
                .withEventId(eventId.toString())
                .withStatus(Status.IN_PROGRESS.name())
                .build();

        UserDTO user = UserDTOFactory.createDefault().withName("Accepted User").build();
        String eventName = "Accepted Event";

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userService.getUserByUserId(userId)).thenReturn(user);
        when(applicationRepository.getEventName(eventId)).thenReturn(eventName);
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationDTO result = service.acceptApplication(applicationId);

        ArgumentCaptor<ApplicationDTO> captor = ArgumentCaptor.forClass(ApplicationDTO.class);
        verify(applicationRepository).save(captor.capture());

        ApplicationDTO capturedApplication = captor.getValue();
        assertThat(capturedApplication.getStatus()).isEqualTo(Status.CONTRACT.name());
        assertThat(capturedApplication.getEventTitle()).isEqualTo("Accepted Event");
        assertThat(capturedApplication.getExhibitorName()).isEqualTo("Accepted User");

        assertThat(result.getStatus()).isEqualTo(Status.CONTRACT.name());
        assertThat(result.getEventTitle()).isEqualTo("Accepted Event");
        assertThat(result.getExhibitorName()).isEqualTo("Accepted User");
    }


    @Test
    @DisplayName("should reject application and ensure eventTitle and exhibitorName are set")
    void rejectApplicationShouldSetFields() {
        UUID applicationId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withApplicationId(applicationId.toString())
                .withUserId(userId.toString())
                .withEventId(eventId.toString())
                .withStatus(Status.IN_PROGRESS.name())
                .build();

        UserDTO user = UserDTOFactory.createDefault().withName("Rejected User").build();
        String eventName = "Rejected Event";

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userService.getUserByUserId(userId)).thenReturn(user);
        when(applicationRepository.getEventName(eventId)).thenReturn(eventName);
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationDTO result = service.rejectApplication(applicationId);

        ArgumentCaptor<ApplicationDTO> captor = ArgumentCaptor.forClass(ApplicationDTO.class);
        verify(applicationRepository).save(captor.capture());

        ApplicationDTO capturedApplication = captor.getValue();
        assertThat(capturedApplication.getStatus()).isEqualTo(Status.REJECTED.name());
        assertThat(capturedApplication.getEventTitle()).isEqualTo("Rejected Event");
        assertThat(capturedApplication.getExhibitorName()).isEqualTo("Rejected User");

        assertThat(result.getStatus()).isEqualTo(Status.REJECTED.name());
        assertThat(result.getEventTitle()).isEqualTo("Rejected Event");
        assertThat(result.getExhibitorName()).isEqualTo("Rejected User");
    }

    @Test
    @DisplayName("Should throw InvalidInputException when exhibitorId is null")
    void shouldThrowExceptionWhenExhibitorIdIsNull() {
        assertThatThrownBy(() -> service.getAllApplicationsByExhibitor(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Exhibitor ID must not be null");
    }

    @Test
    @DisplayName("Should throw NotFoundException when exhibitor does not exist")
    void shouldThrowExceptionWhenExhibitorDoesNotExist() {
        UUID exhibitorId = UUID.randomUUID();
        when(userService.existsByUserAndRole(exhibitorId, Role.EXHIBITOR)).thenReturn(false);

        assertThatThrownBy(() -> service.getAllApplicationsByExhibitor(exhibitorId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Exhibitor not found for ID: " + exhibitorId);
    }

    @Test
    @DisplayName("Should return empty list when exhibitor has no applications")
    void shouldReturnEmptyListWhenExhibitorHasNoApplications() {
        UUID exhibitorId = UUID.randomUUID();
        when(userService.existsByUserAndRole(exhibitorId, Role.EXHIBITOR)).thenReturn(true);
        when(applicationRepository.getAllApplicationsByExhibitorId(exhibitorId)).thenReturn(List.of());

        List<ApplicationDTO> result = service.getAllApplicationsByExhibitor(exhibitorId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return applications with eventTitle and exhibitorName")
    void shouldReturnApplicationsWithEventTitleAndExhibitorName() {
        UUID exhibitorId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withUserId(userId.toString())
                .withEventId(eventId.toString())
                .build();

        when(userService.existsByUserAndRole(exhibitorId, Role.EXHIBITOR)).thenReturn(true);
        when(applicationRepository.getAllApplicationsByExhibitorId(exhibitorId)).thenReturn(List.of(application));
        when(applicationRepository.getEventName(eventId)).thenReturn("Test Event");
        when(userService.getUserByUserId(userId)).thenReturn(UserDTOFactory.createDefault().withName("Exhibitor Name").build());

        List<ApplicationDTO> result = service.getAllApplicationsByExhibitor(exhibitorId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEventTitle()).isEqualTo("Test Event");
        assertThat(result.get(0).getExhibitorName()).isEqualTo("Exhibitor Name");
    }
}

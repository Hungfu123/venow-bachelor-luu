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
    ApplicationRepositoryImpl applicationRepository;

    @InjectMocks
    ApplicationService service;

    @Test
    @DisplayName("should throw exception when exhibitor already applied for the event")
    void shouldThrowExceptionWhenCreateApplication() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(applicationDTO.getUserId(), applicationDTO.getEventId()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.createApplication(applicationDTO))
                .isInstanceOf(DuplicateApplicationException.class)
                .hasMessageContaining("User has already applied for this event");
    }

    @Test
    @DisplayName("should create a new application successfully when no duplicate exists")
    void shouldCreateApplicationSuccessfully() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO);

        assertThat(service.createApplication(applicationDTO)).isEqualTo(applicationDTO);
    }
    @Test
    @DisplayName("Should throw exception when eventId is null")
    void shouldThrowExceptionWhenEventIdIsNull() {
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
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
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO);

        ApplicationDTO result = service.createApplication(applicationDTO);

        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @DisplayName("Should allow user to apply for multiple different events")
    void shouldAllowApplicationForDifferentEvents() {

        UserDTO user = UserDTOFactory.createDefault().build();
        EventDTO event1 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle("Event 1").build();
        EventDTO event2 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle("Event2").build();

        ApplicationDTO applicationDTO1 = ApplicationDTO.builder().userId(user.userId()).eventId(event1.eventId()).build();
        ApplicationDTO applicationDTO2 = ApplicationDTO.builder().userId(user.userId()).eventId(event2.eventId()).build();

        when(applicationRepository.existsByUserIdAndEventId(user.userId(), event1.eventId())).thenReturn(false);
        when(applicationRepository.existsByUserIdAndEventId(user.userId(), event2.eventId())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO1).thenReturn(applicationDTO2);

        ApplicationDTO result1 = service.createApplication(applicationDTO1);
        ApplicationDTO result2 = service.createApplication(applicationDTO2);

        assertThat(result1.getEventId()).isEqualTo(event1.eventId());
        assertThat(result2.getEventId()).isEqualTo(event2.eventId());
    }

    @Test
    @DisplayName("organizer gets all applications")
    void organizerGetsAllApplications() {
        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        List<ApplicationDTO> expectedApplications = List.of(application1, application2);

        when(applicationRepository.getAllApplicationsByOrganizer(any()))
                .thenReturn(expectedApplications);

        List<ApplicationDTO> result = service.getAllApplicationsByOrganizer(any());

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(application1, application2);
    }

    @Test
    @DisplayName("Should return empty list when organizer has no applications")
    void shouldReturnEmptyListWhenNoApplications() {
        UUID organizerId = UUID.randomUUID();

        when(applicationRepository.getAllApplicationsByOrganizer(organizerId))
                .thenReturn(List.of());

        List<ApplicationDTO> result = service.getAllApplicationsByOrganizer(organizerId);

        assertThat(result)
                .isEmpty();
    }

    @Test
    @DisplayName("organizer gets application")
    void organizerGetsApplications() throws InvalidInputException {
        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId(UUID.randomUUID().toString())
                .build();

        when(applicationRepository.findById(any()))
                .thenReturn(Optional.ofNullable(application1));


        assert application1 != null;
        ApplicationDTO application = service.getApplication(application1.getApplicationId());

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
        UUID nonExistentId = UUID.randomUUID();

        when(applicationRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplication(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Application not found");
    }

    @Test
    @DisplayName("organizer reject application")
    void organizerRejectApplication() {

        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        when(applicationRepository.save(any())).thenReturn(applicationDTO);

        ApplicationDTO app = service.rejectApplication(applicationDTO1.get().getApplicationId());

        assertThat(app.getStatus()).isEqualTo("REJECTED");
    }
    @Test
    @DisplayName("organizer reject already rejected application")
    void organizerRejectAlreadyRejectedApplication() {

        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("REJECTED");
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        assertThatThrownBy(() -> service.rejectApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already rejected");
    }

    @Test
    @DisplayName("organizer accept application")
    void organizerAcceptApplication() {

        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        when(applicationRepository.save(any())).thenReturn(applicationDTO);

        ApplicationDTO app = service.acceptApplication(applicationDTO1.get().getApplicationId());

        assertThat(app.getStatus()).isEqualTo(Status.ACCEPTED.toString());
    }

    @Test
    @DisplayName("organizer accept already accepted application")
    void organizerAcceptAlreadyAcceptedApplication() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("ACCEPTED");
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("organizer accept already accepted application")
    void organizerAcceptAlreadyRejectedApplication() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("REJECTED");
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }

    @Test
    @DisplayName("organizer reject already accepted application")
    void organizerRejectAlreadyAcceptedApplication() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        applicationDTO.setStatus("ACCEPTED");
        Optional<ApplicationDTO> applicationDTO1 = Optional.of(applicationDTO);
        when(applicationRepository.findById(any()))
                .thenReturn(applicationDTO1);

        assertThatThrownBy(() -> service.acceptApplication(applicationDTO1.get().getApplicationId()))
                .isInstanceOf(AlreadyRejectedException.class)
                .hasMessageContaining("Application already accepted or rejected");
    }


}

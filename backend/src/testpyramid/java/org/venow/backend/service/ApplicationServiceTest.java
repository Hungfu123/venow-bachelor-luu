package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.applicationservice.*;
import org.venow.backend.applicationservice.exception.DuplicateApplicationException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    Applicationservice service;

    @Test
    @DisplayName("should throw exception when exhibitor already applied for the event")
    void shouldThrowExceptionWhenCreateApplication() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(applicationDTO.userId(), applicationDTO.eventId()))
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
    @DisplayName("Should set initial status to IN_PROGRESS")
    void shouldSetInitialStatus() {
        ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(applicationDTO);

        ApplicationDTO result = service.createApplication(applicationDTO);

        assertThat(result.status()).isEqualTo("IN_PROGRESS");
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

        assertThat(result1.eventId()).isEqualTo(event1.eventId());
        assertThat(result2.eventId()).isEqualTo(event2.eventId());
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

}

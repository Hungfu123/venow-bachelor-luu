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

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        when(applicationRepository.existsByUserIdAndEventId(any(), any())).thenReturn(true);

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

}

package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.infrastructure.EventRepositoryImpl;
import org.venow.backend.testdata.EventDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepositoryImpl eventRepository;

    @InjectMocks
    private Eventservice eventService;

    @DisplayName("should return all events")
    @Test
    void shouldReturnAllEvents() {
        List<EventDTO> events = List.of(EventDTOFactory.createDefault().build(),
                EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).build());

        when(eventRepository.findAll()).thenReturn(events);

        List<EventDTO> result = eventService.getAllEvents();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(EventDTO::title).containsExactly("Test Event", "Test Event");

        verify(eventRepository, times(1)).findAll();
    }

    @DisplayName("should return an event by eventId")
    @Test
    void shouldReturnEventByEventId() {
        UUID eventId = UUID.randomUUID();
        EventDTO event = EventDTOFactory.createDefault().withEventId(eventId).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventDTO result = eventService.getEvent(eventId);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Event");
        assertThat(result.description()).isEqualTo("Test Description");

        verify(eventRepository, times(1)).findById(eventId);
    }

    @DisplayName("should throw NotFoundException if eventId does not exist")
    @Test
    void shouldThrowNotFoundExceptionIfEventIdDoesNotExist() {
        UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEvent(eventId))
                .isInstanceOf(NotFoundException.class);

        verify(eventRepository, times(1)).findById(eventId);
    }

    @DisplayName("should return all events by organizerId")
    @Test
    void shouldReturnAllEventsByOrganizerId() {
        UUID organizerId = UUID.randomUUID();
        List<EventDTO> events = List.of(EventDTOFactory.createDefault().withOrganizerId(organizerId).build(),
                EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withOrganizerId(organizerId).build());

        when(eventRepository.findAllByOrganizerId(organizerId)).thenReturn(events);

        List<EventDTO> result = eventService.getAllEventsByOrganizer(organizerId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(EventDTO::title).containsExactly("Test Event", "Test Event");

        verify(eventRepository, times(1)).findAllByOrganizerId(organizerId);
    }


}

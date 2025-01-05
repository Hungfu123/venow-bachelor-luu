package org.venow.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.infrastructure.EventRepositoryImpl;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserService userService;

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

    @DisplayName("should return all events by search title")
    @Test
    void shouldReturnAllEventsBySearchTitle() {
        final String query = "Test";
        final List<EventDTO> events = List.of(EventDTOFactory.createDefault().withTitle("Test Event").build(),
                EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle("Test Event 2").build());
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        when(eventRepository.findAllByTitleContaining(query)).thenReturn(events);
        when(userService.getUserByUserId(any())).thenReturn(organizer);
        final List<EventDTO> result = eventService.getEventsBySearchTitle(query);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(EventDTO::title).containsExactly("Test Event", "Test Event 2");
        assertThat(result).extracting(EventDTO::organizerName).containsExactly(organizer.name(), organizer.name());
        verify(eventRepository, times(1)).findAllByTitleContaining(query);
    }

    @DisplayName("should return an empty list if search title is less than 3 characters")
    @Test
    void shouldReturnEmptyListIfSearchTitleIsLessThan3Characters() {
        final String query = "Te";
        final List<EventDTO> result = eventService.getEventsBySearchTitle(query);

        assertThat(result).isEmpty();

        verifyNoInteractions(eventRepository);
    }

    @DisplayName("should return an empty list if search title is less than 3 characters")
    @Test
    void shouldReturnEmptyListIfSearchTitleIsLessThan3Characters2() {
        final String query = "T";
        final List<EventDTO> result = eventService.getEventsBySearchTitle(query);

        assertThat(result).isEmpty();

        verifyNoInteractions(eventRepository);
    }


    @DisplayName("should return an event by eventId")
    @Test
    void shouldReturnEventByEventId() {
        UUID eventId = UUID.randomUUID();
        final EventDTO event = EventDTOFactory.createDefault().withEventId(eventId).build();
        final UserDTO organizer = UserDTOFactory.createDefault().withUserId(event.organizerId()).build();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userService.getUserByUserId(event.organizerId())).thenReturn(organizer);

        final EventDTO result = eventService.getEvent(eventId);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Event");
        assertThat(result.description()).isEqualTo("Test Description");

        verify(eventRepository, times(1)).findById(eventId);
    }

    @DisplayName("should throw NotFoundException if eventId does not exist")
    @Test
    void shouldThrowNotFoundExceptionIfEventIdDoesNotExist() {
        final UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEvent(eventId))
                .isInstanceOf(NotFoundException.class);

        verify(eventRepository, times(1)).findById(eventId);
    }

    @DisplayName("should return all events by organizerId")
    @Test
    void shouldReturnAllEventsByOrganizerId() {
        final UUID organizerId = UUID.randomUUID();
        final List<EventDTO> events = List.of(EventDTOFactory.createDefault().withOrganizerId(organizerId).build(),
                EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withOrganizerId(organizerId).build());
        final UserDTO organizer = UserDTOFactory.createDefault().withUserId(organizerId).build();

        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(true);
        when(eventRepository.findAllByOrganizerId(organizerId)).thenReturn(events);
        when(userService.getUserByUserId(organizerId)).thenReturn(organizer);
        final List<EventDTO> result = eventService.getAllEventsByOrganizer(organizerId, Role.ORGANIZER);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(EventDTO::title).containsExactly("Test Event", "Test Event");

        verify(eventRepository, times(1)).findAllByOrganizerId(organizerId);
    }

    @DisplayName("should throw NotFoundException if organizerId does not exist")
    @Test
    void shouldThrowNotFoundExceptionIfOrganizerIdDoesNotExist() {
        final UUID organizerId = UUID.randomUUID();
        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(false);

        assertThatThrownBy(() -> eventService.getAllEventsByOrganizer(organizerId, Role.ORGANIZER))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).existsByUserAndRole(organizerId, Role.ORGANIZER);

    }

    @DisplayName("should create an event")
    @Test
    void shouldCreateEvent() {
        final UUID organizerId = UUID.randomUUID();
        final EventDTO event = EventDTOFactory.createDefault().build();
        final UserDTO organizer = UserDTOFactory.createDefault().withUserId(organizerId).build();

        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(true);
        when(userService.getUserByUserId(organizerId)).thenReturn(organizer);
        when(eventRepository.save(event)).thenReturn(event);

        final EventDTO result = eventService.createEvent(event, organizerId);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Event");
        assertThat(result.description()).isEqualTo("Test Description");

        verify(eventRepository, times(1)).save(event);
    }

    @DisplayName("should throw NotFoundException if organizerId does not exist")
    @Test
    void shouldThrowNotFoundExceptionIfOrganizerIdDoesNotExistByCreate() {
        final UUID organizerId = UUID.randomUUID();
        final EventDTO event = EventDTOFactory.createDefault().build();

        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(false);

        assertThatThrownBy(() -> eventService.createEvent(event, organizerId))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).existsByUserAndRole(organizerId, Role.ORGANIZER);
    }

    @DisplayName("should update an event")
    @Test
    void shouldUpdateEvent() {
        final UUID organizerId = UUID.randomUUID();
        final UserDTO organizer = UserDTOFactory.createDefault().withUserId(organizerId).build();
        final EventDTO event = EventDTOFactory.createDefault().withOrganizerId(organizerId).withOrganizerName(organizer.name()).build();
        final EventDTO retrievedEvent = EventDTOFactory.createDefault().withEventId(event.eventId()).withOrganizerId(organizerId).build();

        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(true);
        when(userService.getUserByUserId(organizerId)).thenReturn(organizer);
        when(eventRepository.findById(event.eventId())).thenReturn(Optional.of(retrievedEvent));
        when(eventRepository.save(event)).thenReturn(event);

        final EventDTO result = eventService.updateEvent(event, organizerId);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Event");
        assertThat(result.description()).isEqualTo("Test Description");

        verify(eventRepository, times(1)).save(event);
    }

    @DisplayName("should throw NotFoundException if organizerId does not exist")
    @Test
    void shouldThrowNotFoundExceptionIfOrganizerIdDoesNotExistByUpdate() {
        final UUID organizerId = UUID.randomUUID();
        final EventDTO event = EventDTOFactory.createDefault().build();

        when(userService.existsByUserAndRole(organizerId, Role.ORGANIZER)).thenReturn(false);

        assertThatThrownBy(() -> eventService.updateEvent(event, organizerId))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).existsByUserAndRole(organizerId, Role.ORGANIZER);
    }




}

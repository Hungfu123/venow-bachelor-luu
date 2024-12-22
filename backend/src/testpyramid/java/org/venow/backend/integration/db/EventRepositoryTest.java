package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.infrastructure.EventRepositoryImpl;
import org.venow.backend.testdata.EventDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventRepositoryTest extends TestBase {

    @Autowired
    private EventRepositoryImpl eventRepository;

    @DisplayName("should save and retrieve event by ID")
    @Test
    void shouldSaveAndRetrieveEventById() {
        EventDTO event = EventDTOFactory.createDefault().build();
        eventRepository.save(event);

        Optional<EventDTO> retrievedEvent = eventRepository.findById(event.eventId());

        assertThat(retrievedEvent).isPresent();
        assertThat(retrievedEvent.get().title()).isEqualTo(event.title());
        assertThat(retrievedEvent.get().startDate()).isEqualTo(event.startDate());
        assertThat(retrievedEvent.get().endDate()).isEqualTo(event.endDate());
    }

    @DisplayName("should return empty optional if event is not found")
    @Test
    void shouldReturnEmptyOptionalIfEventNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<EventDTO> retrievedEvent = eventRepository.findById(nonExistentId);

        assertThat(retrievedEvent).isEmpty();
    }

    @DisplayName("should find all events")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql"})
    void shouldFindAllEventsByName() {
        String eventName = "Tech Expo";
        EventDTO event1 = EventDTOFactory.createDefault().build();
        EventDTO event2 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withTitle(eventName).build();
        eventRepository.save(event1);
        eventRepository.save(event2);

        List<EventDTO> events = eventRepository.findAll();

        assertThat(events).hasSize(2);
        assertThat(events).extracting(EventDTO::title).containsOnly(event1.title(), eventName);
    }

    @DisplayName("should find all events by organizerId")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql"})
    void shouldFindAllEventsByOrganizerId() {
        UUID organizerId = UUID.randomUUID();
        EventDTO event1 = EventDTOFactory.createDefault().withOrganizerId(organizerId).build();
        EventDTO event2 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withOrganizerId(organizerId).build();
        eventRepository.save(event1);
        eventRepository.save(event2);

        List<EventDTO> events = eventRepository.findAllByOrganizerId(organizerId);

        assertThat(events).hasSize(2);
        assertThat(events).extracting(EventDTO::organizerId).containsOnly(organizerId);
    }
}

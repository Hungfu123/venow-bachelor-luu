package org.venow.backend.testdata;

import org.venow.backend.service.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventDTOFactory {

    public static EventDTOWith createDefault() {
        EventDTO defaultEvent = EventDTO.builder()
                .eventId(UUID.randomUUID())
                .title("Test Event")
                .description("Test Description")
                .startDate(LocalDateTime.of(2024,1,1,8,2))
                .endDate(LocalDateTime.of(2024,1,3,8,2))
                .location("Duesseldorf")
                .organizerId(UUID.randomUUID())
                .build();

        return new EventDTOWith(defaultEvent);
    }

    public static class EventDTOWith {
        private EventDTO eventDTO;

        public EventDTOWith(EventDTO eventDTO) {
            this.eventDTO = eventDTO;
        }

        public EventDTOWith withTitle(String title) {
            this.eventDTO = eventDTO.toBuilder().title(title).build();
            return this;
        }

        public EventDTOWith withDescription(String description) {
            this.eventDTO = eventDTO.toBuilder().description(description).build();
            return this;
        }

        public EventDTOWith withEventId(UUID eventId) {
            this.eventDTO = eventDTO.toBuilder().eventId(eventId).build();
            return this;
        }

        public EventDTOWith withOrganizerId(UUID organizerId) {
            this.eventDTO = eventDTO.toBuilder().eventId(organizerId).build();
            return this;
        }

        public EventDTO build() {
            return this.eventDTO;
        }
    }
}

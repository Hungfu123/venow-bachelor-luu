package org.venow.backend.testdata;

import org.venow.backend.applicationservice.EventDTO;

import java.util.UUID;

public class EventDTOFactory {

    public static EventDTOWith createDefault() {
        EventDTO defaultEvent = EventDTO.builder()
                .eventId(UUID.randomUUID())
                .title("Test Event")
                .description("Test Description")
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

        public EventDTO build() {
            return this.eventDTO;
        }
    }
}

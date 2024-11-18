package org.venow.backend.testdata;

import org.venow.backend.applicationservice.EventDTO;

import java.util.UUID;

public class EventDTOFactory {

    public static EventDTO.EventDTOBuilder defaultBuilder() {
        return EventDTO.builder()
                .eventId(UUID.randomUUID())
                .title("Test Event")
                .description("Test Description");
    }

    public static EventDTO createDefault() {
        return defaultBuilder().build();
    }

    public static EventDTO createWithCustomTitle(String title) {
        return defaultBuilder().title(title).build();
    }
}

package org.venow.backend.web.reponse;

import org.venow.backend.applicationservice.EventDTO;

public class ResponseMapper {


    public static EventResponse mapToEventResponse(final EventDTO event) {
        return EventResponse.builder()
                .eventId(event.eventId())
                .title(event.title())
                .description(event.description())
                .location(event.location())
                .startDate(event.startDate())
                .endDate(event.endDate())
                .build();
    }
}

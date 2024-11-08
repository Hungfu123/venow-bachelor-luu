package org.venow.backend.applicationservice.mapper;

import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.domain.model.Event;

public class EventMapper {


    public static EventDTO mapToEventDTO(Event event) {
        return EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}

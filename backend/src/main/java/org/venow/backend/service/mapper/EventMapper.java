package org.venow.backend.service.mapper;

import org.venow.backend.service.dto.EventDTO;
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
                .organizerId(event.getOrganizerId())
                .build();
    }

    public static Event mapToEntity(EventDTO eventDTO) {
        return Event.builder()
                .eventId(eventDTO.eventId())
                .title(eventDTO.title())
                .description(eventDTO.description())
                .location(eventDTO.location())
                .startDate(eventDTO.startDate())
                .endDate(eventDTO.endDate())
                .organizerId(eventDTO.organizerId())
                .build();
    }
}

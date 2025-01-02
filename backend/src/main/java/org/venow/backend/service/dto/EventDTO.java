package org.venow.backend.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record EventDTO( Long id, UUID eventId,
                       String title,
                       String description,
                       String location,
                       LocalDateTime startDate,
                       LocalDateTime endDate,
                       String organizerName,
                       Integer booths,
                       UUID organizerId
) {
}

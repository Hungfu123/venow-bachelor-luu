package org.venow.backend.applicationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record EventDTO(UUID eventId,
                       String title,
                       String description,
                       String location,
                       LocalDateTime startDate,
                       LocalDateTime endDate,
                       UUID organizerId
) {
}

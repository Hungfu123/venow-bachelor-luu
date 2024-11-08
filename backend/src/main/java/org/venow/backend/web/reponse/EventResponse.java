package org.venow.backend.web.reponse;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EventResponse(
        UUID eventId,
        String title,
        String description,
        String location,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

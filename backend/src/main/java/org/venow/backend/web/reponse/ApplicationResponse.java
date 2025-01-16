package org.venow.backend.web.reponse;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ApplicationResponse(
        Long id,
        UUID applicationId,
        UUID userId,
        UUID eventId,
        String eventTitle,
        String exhibitorName,
        String contactInformation,
        String businessName,
        String typeOfBusiness,
        boolean previousParticipation,
        String message,
        String status
) {}

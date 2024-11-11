package org.venow.backend.web.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ApplicationRequest(
        Long id,
        UUID applicationId,
        UUID userId,
        UUID eventId,
        String contactInformation,
        String businessName,
        String typeOfBusiness,
        boolean previousParticipation,
        String message,
        String status
) {}


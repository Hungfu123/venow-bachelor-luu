package org.venow.backend.applicationservice;

import lombok.Builder;

import java.util.UUID;
@Builder(toBuilder = true)
public record ApplicationDTO(
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
) {

}

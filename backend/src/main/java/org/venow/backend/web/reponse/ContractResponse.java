package org.venow.backend.web.reponse;

import org.venow.backend.service.dto.PriceDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractResponse(
        UUID contractId,
        UUID organizerId,
        String applicantName,
        String organizerName,
        String eventName,
        String location,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String typeOfBusiness,
        String contactInformation,
        String businessName,
        String terms,
        PriceDto price,
        String status,
        String organizerEmail

){

}

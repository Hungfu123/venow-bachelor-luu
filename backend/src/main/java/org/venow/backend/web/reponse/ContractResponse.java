package org.venow.backend.web.reponse;

import org.venow.backend.applicationservice.dto.PriceDto;

import java.util.UUID;

public record ContractResponse(
        Long id,
        UUID contractId,
        String applicantName,
        String organizerName,
        String eventName,
        String typeOfBusiness,
        String contactInformation,
        String businessName,
        String terms,
        PriceDto price,
        String status,
        String organizerEmail

){

}

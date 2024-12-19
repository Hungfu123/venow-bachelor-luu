package org.venow.backend.web.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.venow.backend.service.dto.PriceDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
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
        String boothNumber,
        String status,
        String organizerEmail

){
    @JsonProperty("price")
    public double getPriceAsDouble() {
        return price != null ? price.getAmountInEuro().doubleValue() : 0.0;
    }
}

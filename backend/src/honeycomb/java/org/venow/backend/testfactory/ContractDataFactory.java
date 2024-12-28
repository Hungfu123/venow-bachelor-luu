package org.venow.backend.testfactory;

import org.venow.backend.service.dto.PriceDto;
import org.venow.backend.web.reponse.ContractResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ContractDataFactory {

    public static ContractResponse createFirstContractResponse() {
        return ContractResponse.builder()
                .contractId(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"))
                .organizerId(UUID.fromString("b23e4567-e89b-12d3-a456-426614174002"))
                .applicationId(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2346"))
                .exhibitorId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .applicantName("Khang")
                .organizerName("Robert J")
                .eventName("Test Event 3")
                .location("Mühlheim")
                .startDate(LocalDateTime.parse("2024-06-01T12:00:00"))
                .endDate(LocalDateTime.parse("2024-06-01T23:00:00"))
                .typeOfBusiness("Software IT")
                .contactInformation("dfadf@example.com")
                .businessName("IT Technik")
                .terms("Basic Terms")
                .price(new PriceDto(300))
                .boothNumber("A10")
                .status("CONTRACT")
                .organizerEmail("organizer3@example.com")
                .build();
    }

    public static ContractResponse createSecondContractResponse() {
        return ContractResponse.builder()
                .contractId(UUID.fromString("34e433de-8d61-48e7-b74d-904ae6de0d2a"))
                .organizerId(UUID.fromString("b23e4567-e89b-12d3-a456-426614174002"))
                .exhibitorId(UUID.fromString("eb7a9506-25d5-422a-842e-938008c448d6"))
                .applicationId(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2348"))
                .applicantName("Zig Arette")
                .organizerName("Robert J")
                .eventName("Test Event 5")
                .location("Berlin")
                .startDate(LocalDateTime.parse("2024-06-01T12:00:00"))
                .endDate(LocalDateTime.parse("2024-06-01T23:00:00"))
                .typeOfBusiness("Software IT")
                .contactInformation("dfadf@example.com")
                .businessName("IT Technik")
                .terms("Basic Terms")
                .price(new PriceDto(300))
                .boothNumber("A10")
                .status("CONTRACT")
                .organizerEmail("organizer3@example.com")
                .build();
    }

    public static ContractResponse createThirdContractResponse() {
        return ContractResponse.builder()
                .contractId(UUID.fromString("9452c6a7-6acc-4208-bbb3-f04396326fc2"))
                .organizerId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .exhibitorId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .applicationId(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2345"))
                .applicantName("Khang")
                .organizerName("Frank Stein")
                .eventName("Test")
                .location("Test Park")
                .startDate(LocalDateTime.parse("2024-06-01T12:00:00"))
                .endDate(LocalDateTime.parse("2024-06-01T23:00:00"))
                .typeOfBusiness("Software IT")
                .contactInformation("dfadf@example.com")
                .businessName("IT Technik")
                .terms("Basic Terms")
                .price(new PriceDto(300))
                .boothNumber("A10")
                .status("REJECTED")
                .organizerEmail("organizer1@example.com")
                .build();
    }


    public static List<ContractResponse> getAllContractResponses() {
        return List.of(
                createFirstContractResponse(),
                createSecondContractResponse()
        );
    }

    public static List<ContractResponse> getAllContractResponseExhibitor() {
        return List.of(
                createFirstContractResponse(),
                createThirdContractResponse()
        );
    }
}

package org.venow.backend.web.reponse;

import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.EventDTO;

import java.net.URI;
import java.time.LocalDateTime;

public class ResponseMapper {


    public static EventResponse mapToEventResponse(final EventDTO event) {
        return EventResponse.builder()
                .eventId(event.eventId())
                .title(event.title())
                .description(event.description())
                .location(event.location())
                .startDate(event.startDate())
                .endDate(event.endDate())
                .organizerName(event.organizerName())
                .booths(event.booths())
                .build();
    }
    public static ApplicationResponse mapToApplicationResponse(final ApplicationDTO application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .applicationId(application.getApplicationId())
                .userId(application.getUserId())
                .eventId(application.getEventId())
                .contactInformation(application.getContactInformation())
                .businessName(application.getBusinessName())
                .typeOfBusiness(application.getTypeOfBusiness())
                .previousParticipation(application.isPreviousParticipation())
                .message(application.getMessage())
                .status(application.getStatus())
                .build();
    }


    public static ContractResponse mapToContractResponse(ContractDTO contract) {
        return ContractResponse.builder()
                .contractId(contract.getContractId())
                .organizerId(contract.getOrganizer().userId())
                .exhibitorId(contract.getExhibitor().userId())
                .applicationId(contract.getApplication().getApplicationId())
                .applicantName(contract.getApplicantName())
                .organizerName(contract.getOrganizerName())
                .eventName(contract.getEventName())
                .location(contract.getLocation())
                .startDate(LocalDateTime.parse(contract.getStartDate()))
                .endDate(LocalDateTime.parse(contract.getEndDate()))
                .typeOfBusiness(contract.getApplication().getTypeOfBusiness())
                .contactInformation(contract.getApplication().getContactInformation())
                .businessName(contract.getApplication().getBusinessName())
                .terms(contract.getTerms())
                .price(contract.getPrice())
                .boothNumber(contract.getBoothNumber())
                .status(contract.getStatus())
                .organizerEmail(contract.getOrganizerEmail())
                .build();

    }
}

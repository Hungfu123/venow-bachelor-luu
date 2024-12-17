package org.venow.backend.web.reponse;

import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;

public class ResponseMapper {


    public static EventResponse mapToEventResponse(final EventDTO event) {
        return EventResponse.builder()
                .eventId(event.eventId())
                .title(event.title())
                .description(event.description())
                .location(event.location())
                .startDate(event.startDate())
                .endDate(event.endDate())
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


}

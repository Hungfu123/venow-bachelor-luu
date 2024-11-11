package org.venow.backend.web.reponse;

import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.EventDTO;

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
                .id(application.id())
                .applicationId(application.applicationId())
                .userId(application.userId())
                .eventId(application.eventId())
                .contactInformation(application.contactInformation())
                .businessName(application.businessName())
                .typeOfBusiness(application.typeOfBusiness())
                .previousParticipation(application.previousParticipation())
                .message(application.message())
                .status(application.status())
                .build();
    }

}

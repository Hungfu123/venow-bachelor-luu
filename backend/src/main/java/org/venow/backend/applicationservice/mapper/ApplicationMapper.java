package org.venow.backend.applicationservice.mapper;

import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.domain.model.Application;
import org.venow.backend.domain.model.Status;
import org.venow.backend.web.reponse.ApplicationResponse;

public class ApplicationMapper {

    public static ApplicationDTO mapToDTO(Application application) {
        return new ApplicationDTO(
                application.getId(),
                application.getApplicationId(),
                application.getUserId(),
                application.getEventId(),
                application.getContactInformation(),
                application.getBusinessName(),
                application.getTypeOfBusiness(),
                application.isPreviousParticipation(),
                application.getMessage(),
                application.getStatus().toString()
        );
    }

    public static Application mapToEntity(ApplicationDTO applicationDTO) {
        return new Application(
                applicationDTO.id(),
                applicationDTO.applicationId(),
                applicationDTO.userId(),
                applicationDTO.eventId(),
                applicationDTO.contactInformation(),
                applicationDTO.businessName(),
                applicationDTO.typeOfBusiness(),
                applicationDTO.previousParticipation(),
                applicationDTO.message(),
                Status.valueOf(applicationDTO.status())
        );
    }

    public static ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getApplicationId(),
                application.getUserId(),
                application.getEventId(),
                application.getContactInformation(),
                application.getBusinessName(),
                application.getTypeOfBusiness(),
                application.isPreviousParticipation(),
                application.getMessage(),
                application.getStatus().toString()
        );
    }
}


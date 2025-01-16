package org.venow.backend.service.mapper;

import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.domain.model.Application;
import org.venow.backend.domain.model.Status;
import org.venow.backend.web.reponse.ApplicationResponse;

public class ApplicationMapper {

    public static ApplicationDTO mapToDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .applicationId(application.getApplicationId())
                .userId(application.getUserId())
                .eventId(application.getEventId())
                .contactInformation(application.getContactInformation())
                .businessName(application.getBusinessName())
                .typeOfBusiness(application.getTypeOfBusiness())
                .previousParticipation(application.isPreviousParticipation())
                .message(application.getMessage())
                .status(application.getStatus().toString())
                .build();
    }

    public static Application mapToEntity(ApplicationDTO applicationDTO) {
        return new Application(
                applicationDTO.getId(),
                applicationDTO.getApplicationId(),
                applicationDTO.getUserId(),
                applicationDTO.getEventId(),
                applicationDTO.getContactInformation(),
                applicationDTO.getBusinessName(),
                applicationDTO.getTypeOfBusiness(),
                applicationDTO.isPreviousParticipation(),
                applicationDTO.getMessage(),
                Status.valueOf(applicationDTO.getStatus())
        );
    }


}


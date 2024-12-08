package org.venow.backend.builder;

import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.UUID;

public class ApplicationResponseBuilder {
    private Long id = 1L;
    private UUID applicationId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();
    private UUID eventId = UUID.randomUUID();
    private String contactInformation = "default@example.com";
    private String businessName = "Default Business";
    private String typeOfBusiness = "Default Type";
    private boolean previousParticipation = false;
    private String message = "Default message";
    private String status = "IN_PROGRESS";

    public ApplicationResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ApplicationResponseBuilder withApplicationId(String applicationId) {
        this.applicationId = UUID.fromString(applicationId);
        return this;
    }

    public ApplicationResponseBuilder withUserId(String userId) {
        this.userId = UUID.fromString(userId);
        return this;
    }

    public ApplicationResponseBuilder withEventId(String eventId) {
        this.eventId = UUID.fromString(eventId);
        return this;
    }

    public ApplicationResponseBuilder withContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
        return this;
    }

    public ApplicationResponseBuilder withBusinessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    public ApplicationResponseBuilder withTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
        return this;
    }

    public ApplicationResponseBuilder withPreviousParticipation(boolean previousParticipation) {
        this.previousParticipation = previousParticipation;
        return this;
    }

    public ApplicationResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ApplicationResponseBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public ApplicationResponse build() {
        return ApplicationResponse.builder()
                .id(id)
                .applicationId(applicationId)
                .userId(userId)
                .eventId(eventId)
                .contactInformation(contactInformation)
                .businessName(businessName)
                .typeOfBusiness(typeOfBusiness)
                .previousParticipation(previousParticipation)
                .message(message)
                .status(status)
                .build();
    }
}

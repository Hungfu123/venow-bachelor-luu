package org.venow.backend.testdata;

import org.venow.backend.applicationservice.ApplicationDTO;

import java.util.UUID;

public class ApplicationDTOFactory {

    public static ApplicationDTOWith createDefault() {
        ApplicationDTO defaultApplication = ApplicationDTO.builder()
                .applicationId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .contactInformation("034023423")
                .businessName("Luu Business")
                .typeOfBusiness("Luu Type")
                .previousParticipation(false)
                .message("Nimm mich")
                .status("IN_PROGRESS")
                .build();

        return new ApplicationDTOWith(defaultApplication);
    }

    public static class ApplicationDTOWith {
        private ApplicationDTO applicationDTO;

        public ApplicationDTOWith(ApplicationDTO applicationDTO) {
            this.applicationDTO = applicationDTO;
        }

        public ApplicationDTOWith withApplicationId(UUID applicationId) {
            this.applicationDTO = applicationDTO.toBuilder().applicationId(applicationId).build();
            return this;
        }

        public ApplicationDTOWith withUserId(UUID userId) {
            this.applicationDTO = applicationDTO.toBuilder().userId(userId).build();
            return this;
        }

        public ApplicationDTOWith withEventId(UUID eventId) {
            this.applicationDTO = applicationDTO.toBuilder().eventId(eventId).build();
            return this;
        }

        public ApplicationDTOWith withContactInformation(String contactInformation) {
            this.applicationDTO = applicationDTO.toBuilder().contactInformation(contactInformation).build();
            return this;
        }

        public ApplicationDTOWith withBusinessName(String businessName) {
            this.applicationDTO = applicationDTO.toBuilder().businessName(businessName).build();
            return this;
        }

        public ApplicationDTOWith withTypeOfBusiness(String typeOfBusiness) {
            this.applicationDTO = applicationDTO.toBuilder().typeOfBusiness(typeOfBusiness).build();
            return this;
        }

        public ApplicationDTOWith withPreviousParticipation(boolean previousParticipation) {
            this.applicationDTO = applicationDTO.toBuilder().previousParticipation(previousParticipation).build();
            return this;
        }

        public ApplicationDTOWith withMessage(String message) {
            this.applicationDTO = applicationDTO.toBuilder().message(message).build();
            return this;
        }

        public ApplicationDTOWith withStatus(String status) {
            this.applicationDTO = applicationDTO.toBuilder().status(status).build();
            return this;
        }

        public ApplicationDTO build() {
            return this.applicationDTO;
        }
    }
}

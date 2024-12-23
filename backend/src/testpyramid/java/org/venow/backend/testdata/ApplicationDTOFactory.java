package org.venow.backend.testdata;

import org.venow.backend.service.dto.ApplicationDTO;

import java.util.UUID;

public class ApplicationDTOFactory {

    public static ApplicationDTOWith createDefault() {
        ApplicationDTO defaultApplication = ApplicationDTO.builder()
                .applicationId(UUID.randomUUID())
                .userId(UUID.fromString("df333e19-6ed6-4c8b-b86e-ef78943a98a4"))
                .eventId(UUID.fromString("45353065-3834-3030-2d65-3239633d3431"))
                .contactInformation("example@example.com")
                .businessName("Sample Business")
                .typeOfBusiness("Food Truck")
                .previousParticipation(true)
                .message("Looking forward to participating!")
                .status("IN_PROGRESS")
                .build();

        return new ApplicationDTOWith(defaultApplication);
    }

    public static class ApplicationDTOWith {
        private ApplicationDTO applicationDTO;

        public ApplicationDTOWith(ApplicationDTO applicationDTO) {
            this.applicationDTO = applicationDTO;
        }

        public ApplicationDTOWith withId(Long id) {
            this.applicationDTO = applicationDTO.toBuilder().id(id).build();
            return this;
        }
        public ApplicationDTOWith withApplicationId(String applicationId) {
            this.applicationDTO = applicationDTO.toBuilder().applicationId(UUID.fromString(applicationId)).build();
            return this;
        }

        public ApplicationDTOWith withUserId(String userId) {
            this.applicationDTO = applicationDTO.toBuilder().userId(UUID.fromString(userId)).build();
            return this;
        }

        public ApplicationDTOWith withEventId(String eventId) {
            this.applicationDTO = applicationDTO.toBuilder().eventId(UUID.fromString(eventId)).build();
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

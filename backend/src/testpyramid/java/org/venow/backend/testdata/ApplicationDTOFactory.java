package org.venow.backend.testdata;

import org.venow.backend.applicationservice.ApplicationDTO;

import java.util.UUID;

public class ApplicationDTOFactory {

    public static ApplicationDTO.ApplicationDTOBuilder defaultBuilder() {
        return ApplicationDTO.builder()
                .applicationId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .contactInformation("034023423")
                .businessName("Luu Business")
                .typeOfBusiness("Luu Type")
                .previousParticipation(false)
                .message("Nimm mich")
                .status("IN_PROGRESS");
    }

    public static ApplicationDTO createDefault() {
        return defaultBuilder().build();
    }

    public static ApplicationDTO createWithCustomBusinessName(String businessName) {
        return defaultBuilder().businessName(businessName).build();
    }
}

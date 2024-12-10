package org.venow.backend.applicationservice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class ApplicationDTO {
    private Long id;
    private UUID applicationId;
    private UUID userId;
    private UUID eventId;
    private String contactInformation;
    private String businessName;
    private String typeOfBusiness;
    private boolean previousParticipation;
    private String message;
    private String status;

}

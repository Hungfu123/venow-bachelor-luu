package org.venow.backend.service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ApplicationDTO {
    private Long id;
    private UUID applicationId;
    private UUID userId;
    private UUID eventId;
    private String eventTitle;
    private String exhibitorName;
    private String contactInformation;
    private String businessName;
    private String typeOfBusiness;
    private boolean previousParticipation;
    private String message;
    private String status;

}

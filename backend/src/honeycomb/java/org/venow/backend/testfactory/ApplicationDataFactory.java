package org.venow.backend.testfactory;

import org.venow.backend.domain.model.Status;
import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.List;
import java.util.UUID;

public class ApplicationDataFactory {

    public static ApplicationResponse createDefaultApplicationResponse() {
        return ApplicationResponse.builder()
                .id(1L)
                .applicationId(UUID.fromString("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a"))
                .eventId(UUID.fromString("45353065-3834-3030-2d65-3239633d3431"))
                .userId(UUID.fromString("df333e19-6ed6-4c8b-b86e-ef78943a98a4"))
                .contactInformation("example@example.com")
                .businessName("Sample Business")
                .typeOfBusiness("Food Truck")
                .previousParticipation(true)
                .message("Looking forward to participating!")
                .status("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createConsultingApplicationResponse() {
        return ApplicationResponse.builder()
                .id(2L)
                .applicationId(UUID.fromString("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b"))
                .eventId(UUID.fromString("45353065-3834-3030-2d65-3239633d3431"))
                .userId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .contactInformation("dfadf@example.com")
                .businessName("Luu Consults")
                .typeOfBusiness("Consulting")
                .previousParticipation(true)
                .message("Looking forward to participating!")
                .status("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createITApplicationResponse() {
        return ApplicationResponse.builder()
                .id(3L)
                .applicationId(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2345"))
                .eventId(UUID.fromString("45353065-3834-3030-2d65-3239633d3431"))
                .userId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a3"))
                .contactInformation("dfadf@example.com")
                .businessName("IT Technik")
                .typeOfBusiness("Software IT")
                .previousParticipation(true)
                .message("Looking forward to participating!")
                .status("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createOtherOrganizerApplicationResponse() {
        return ApplicationResponse.builder()
                .id(4L)
                .applicationId(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2346"))
                .eventId(UUID.fromString("17353065-3834-3030-2d65-3239633d3431"))
                .userId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .contactInformation("dfadf@example.com")
                .businessName("IT Technik")
                .typeOfBusiness("Software IT")
                .previousParticipation(true)
                .message("Looking forward to participating!")
                .status(Status.CONTRACT.name())
                .build();
    }

    public static List<ApplicationResponse> getAllApplicationResponses() {
        return List.of(
                createDefaultApplicationResponse(),
                createConsultingApplicationResponse(),
                createITApplicationResponse()
        );
    }
}

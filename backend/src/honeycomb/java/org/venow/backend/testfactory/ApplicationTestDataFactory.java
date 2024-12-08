package org.venow.backend.testfactory;


import org.venow.backend.builder.ApplicationResponseBuilder;
import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.List;

public class ApplicationTestDataFactory {

    public static ApplicationResponse createDefaultApplicationResponse() {
        return new ApplicationResponseBuilder()
                .withId(1L)
                .withApplicationId("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .withUserId("df333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .withContactInformation("example@example.com")
                .withBusinessName("Sample Business")
                .withTypeOfBusiness("Food Truck")
                .withPreviousParticipation(true)
                .withMessage("Looking forward to participating!")
                .withStatus("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createConsultingApplicationResponse() {
        return new ApplicationResponseBuilder()
                .withId(2L)
                .withApplicationId("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .withUserId("af333e19-6ed6-4c8b-b86e-ef78943a98a2")
                .withContactInformation("dfadf@example.com")
                .withBusinessName("Luu Consults")
                .withTypeOfBusiness("Consulting")
                .withPreviousParticipation(true)
                .withMessage("Looking forward to participating!")
                .withStatus("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createITApplicationResponse() {
        return new ApplicationResponseBuilder()
                .withId(3L)
                .withApplicationId("c3456789-01cd-ef23-4567-8901cdef2345")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .withUserId("af333e19-6ed6-4c8b-b86e-ef78943a98a3")
                .withContactInformation("dfadf@example.com")
                .withBusinessName("IT Technik")
                .withTypeOfBusiness("Software IT")
                .withPreviousParticipation(true)
                .withMessage("Looking forward to participating!")
                .withStatus("IN_PROGRESS")
                .build();
    }

    public static ApplicationResponse createOtherOrganizerApplicationResponse() {
        return new ApplicationResponseBuilder()
                .withId(4L)
                .withApplicationId("c3456789-01cd-ef23-4567-8901cdef2346")
                .withEventId("17353065-3834-3030-2d65-3239633d3431")
                .withUserId("af333e19-6ed6-4c8b-b86e-ef78943a98a2")
                .withContactInformation("dfadf@example.com")
                .withBusinessName("IT Technik")
                .withTypeOfBusiness("Software IT")
                .withPreviousParticipation(true)
                .withMessage("Looking forward to participating!")
                .withStatus("IN_PROGRESS")
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

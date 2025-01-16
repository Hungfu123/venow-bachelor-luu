package org.venow.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.infrastructure.ApplicationRepositoryImpl;
import org.venow.backend.testfactory.ApplicationDataFactory;
import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.boot.context.properties.bind.Bindable.mapOf;

class OrganizerApplicationControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationRepositoryImpl applicationRepository;

    @DisplayName("get all Applications by an organizer")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void organizerSeeAllHisApplication() throws JsonProcessingException {
        final List<ApplicationResponse> expectedRespones = List.of(ApplicationDataFactory.createDefaultApplicationResponse(),ApplicationDataFactory.createConsultingApplicationResponse(), ApplicationDataFactory.createITApplicationResponse());

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}organizer/{organizerId}/applications",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID.toString());
        assertThat(response.getStatusCode())
                .as("check if HTTP Stastus is 200")
                .isEqualTo(HttpStatus.OK);

        List<ApplicationResponse> actualResponses = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {
                }
        );

        assertThat(actualResponses)
                .as("check if response contains expected applications by organizerId")
                .usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(expectedRespones);

    }

    @DisplayName("Organizer has no applications")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void organizerHasNoApplications() throws JsonProcessingException {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port, ORGANIZER_ID_EMPTY_APP.toString()
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        List<ApplicationResponse> actualResponses = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {}
        );

        assertThat(actualResponses)
                .isEmpty();
    }

    @DisplayName("should not found applications")
    @Test
    void organizershouldNotSeeAllHisApplication() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}organizer/{organizerId}/applications",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, null);
        assertThat(response.getStatusCode())
                .as("check if HTTP Stastus is Not Found")
                .isEqualTo(HttpStatus.NOT_FOUND);

    }
    @Test
    @DisplayName("Invalid organizer ID format")
    void invalidOrganizerIdFormat() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port, "invalid-id"
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should see an application by organizer")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldSeeAnApplicationByOrganizer() throws JsonProcessingException {


        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}",
                HttpMethod.GET,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), APPLICATION_1.applicationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApplicationResponse actualResponses = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {
                }
        );

        assertThat(actualResponses)
                .as("check if response contains expected applications by organizerId")
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(APPLICATION_1);
    }

    @DisplayName("should not get his application")
    @Test
    void organizershouldNotSeeHisApplication() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}organizer/{organizerId}/applications/{applicationId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID_EMPTY_APP.toString(), null);
        assertThat(response.getStatusCode())
                .as("check if HTTP Stastus is Not Found")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should return NOT_FOUND for non-existent applicationId")
    void shouldReturnNotFoundForNonExistentApplicationId() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}",
                HttpMethod.GET,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), UUID.randomUUID().toString()
        );

        assertThat(response.getStatusCode())
                .as("check if HTTP Status is Not Found for invalid applicationId")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should return BAD_REQUEST for invalid applicationId format")
    void shouldReturnBadRequestForInvalidApplicationIdFormat() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}",
                HttpMethod.GET,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), "blabla"
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("reject application and has status rejected")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void rejectApplication() throws JsonProcessingException {

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}/reject",
                HttpMethod.POST,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), APPLICATION_1.applicationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        assertThatJson(response.getBody()).as("check if application contains status rejected").and(
                json -> json.node("applicationId").isEqualTo(APPLICATION_1.applicationId().toString()),
                json -> json.node("userId").isEqualTo("af333e19-6ed6-4c8b-b86e-ef78943a98a3"),
                json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                json -> json.node("contactInformation").isEqualTo("dfadf@example.com"),
                json -> json.node("businessName").isEqualTo("IT Technik"),
                json -> json.node("status").isEqualTo("REJECTED"));


        ApplicationDTO savedApplication = applicationRepository.findById(APPLICATION_1.applicationId()).orElseThrow(
                () -> new NotFoundException("Application not found")
        );

        assertThat(savedApplication.getStatus()).isEqualTo("REJECTED");
    }

    @Test
    @DisplayName("reject application already rejected")
    @Sql(scripts = { "/db/testmigration/002_reject_application.sql"})
    void rejectApplicationAlreadyRejected() throws JsonProcessingException {

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}/reject",
                HttpMethod.POST,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), APPLICATION_1.applicationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        String errorMessage = response.getBody();
        assertThat(errorMessage).isEqualTo("Application already rejected");
    }

    @Test
    @DisplayName("Organizer can see application on event")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void organizerCanSeeApplicationOnEvent() {
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}organizer/{organizerId}/events/{eventId}/applications",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, "123e4567-e89b-12d3-a456-426614174000", "45353065-3834-3030-2d65-3239633d3431");
        assertThat(response.getStatusCode()).as("check if HTTP Status OK").isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .as("check if response contains application")
                .contains("applicationId");
        List<Map<String, Object>> applications = JsonPath.parse(response.getBody()).read("$[*]");

        assertThat(applications).extracting(
                app -> tuple(
                        app.get("id"),
                        app.get("applicationId"),
                        app.get("userId"),
                        app.get("eventId"),
                        app.get("contactInformation"),
                        app.get("businessName"),
                        app.get("typeOfBusiness"),
                        app.get("previousParticipation"),
                        app.get("message"),
                        app.get("status")
                )
        ).containsExactlyInAnyOrder(
                tuple(3, "c3456789-01cd-ef23-4567-8901cdef2345", "af333e19-6ed6-4c8b-b86e-ef78943a98a3", "45353065-3834-3030-2d65-3239633d3431", "dfadf@example.com", "IT Technik", "Software IT", true, "Looking forward to participating!", "IN_PROGRESS"),
                tuple(2, "b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b", "af333e19-6ed6-4c8b-b86e-ef78943a98a2", "45353065-3834-3030-2d65-3239633d3431", "dfadf@example.com", "Luu Consults", "Consulting", true, "Looking forward to participating!", "IN_PROGRESS"),
                tuple(1, "b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a", "df333e19-6ed6-4c8b-b86e-ef78943a98a4", "45353065-3834-3030-2d65-3239633d3431", "example@example.com", "Sample Business", "Food Truck", true, "Looking forward to participating!", "IN_PROGRESS")
        );
    }

    @Test
    @DisplayName("Organizer can see application on event but it is empty")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql" })
    void organizerCanSeeApplicationOnEvent2() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/organizer/{organizerId}/events/{eventId}/applications",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port,
                "123e4567-e89b-12d3-a456-426614174000",
                "67353065-3834-3030-2d65-3239633d3432");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status is OK")
                .isEqualTo(HttpStatus.OK);

        List<Map<String, Object>> applications = JsonPath.parse(response.getBody()).read("$[*]");

        assertThat(applications)
                .as("check if applications list is empty")
                .isEmpty();
    }

    @Test
    @DisplayName("Organizer sees error for invalid event ID")
    void organizerSeesErrorForInvalidEventId() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/organizer/{organizerId}/events/{eventId}/applications",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port,
                "123e4567-e89b-12d3-a456-426614174000",
                "invalid-event-id");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status is BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody())
                .as("check if response contains error message")
                .contains("Invalid UUID string: invalid-event-id");
    }

    @Test
    @DisplayName("Organizer sees error for invalid organizer ID")
    void organizerSeesErrorForInvalidOrganizerId() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/organizer/{organizerId}/events/{eventId}/applications",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port,
                "invalid-organizer-id",
                "45353065-3834-3030-2d65-3239633d3431");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status is BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody())
                .as("check if response contains error message")
                .contains("Invalid UUID string: invalid-organizer-id");
    }


}

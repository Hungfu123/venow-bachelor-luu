package org.venow.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.venow.backend.web.reponse.ApplicationResponse;
import org.venow.backend.web.request.ApplicationRequest;

import java.util.List;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class
ExhibitorApplicationControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("exhibitor gets on application for eventId")
    void applicationGetsOnApplicationPage() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{exhibitorId}/events/{eventId}/application",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, EXHIBITOR_ID_1, EVENT_ID_1);
        assertThat(response.getStatusCode())
                .as("check if HTTP Status 200 OK")
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("exhibitor apply for an event")
    @Sql(scripts = { "/db/testmigration/000_init.sql"})
    void exhibitorApplyForEventPage() {
        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{userId}/events/{eventId}/application",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                String.class,
                port, EXHIBITOR_ID_1, EVENT_ID_1);

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 201 CREATED")
                .isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody())
                .as("check if response contains application")
                .and(
                        json -> json.node("applicationId").isNotNull(),
                        json -> json.node("userId").isEqualTo(EXHIBITOR_ID_1.toString()),
                        json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                        json -> json.node("eventTitle").isEqualTo("Test"),
                        json -> json.node("exhibitorName").isEqualTo(""),
                        json -> json.node("contactInformation").isEqualTo("example@example.com"),
                        json -> json.node("businessName").isEqualTo("Sample Business"),
                        json -> json.node("status").isEqualTo("IN_PROGRESS")
                );
    }

    @Test
    @DisplayName("exhibitor apply for the same event again - CONFLICTED")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void exhibitorApplyForSameEventAgain() {

        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{userId}/events/{eventId}/application",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                String.class,
                port, EXHIBITOR_ID_1, EVENT_ID_1);

        assertThat(response.getStatusCode())
                .as("check if HTTP Status IS CONFLICTED")
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(response.getBody())
                .as("check if response contains message")
                .isEqualTo("User has already applied for this event.");
    }

    @Test
    @DisplayName("another exhibitor apply for the same event")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void anotherExhibitorApplyForSameEvent() {

        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/events/{eventId}/application",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                String.class,
                port, USER_EXHIBITOR_3.getUserId(), EVENT_ID_1);

        assertThat(response.getStatusCode()).as("check if HTTP Status 201 CREATED").isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody()).as("check if response contains application").and(
                json -> json.node("applicationId").isNotNull(),
                json -> json.node("userId").isEqualTo(EXHIBITOR_ID_2.toString()),
                json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                json -> json.node("eventTitle").isEqualTo("Test"),
                json -> json.node("exhibitorName").isEqualTo("Frank Reich"),
                json -> json.node("contactInformation").isEqualTo("example@example.com"),
                json -> json.node("businessName").isEqualTo("Sample Business"),
                json -> json.node("status").isEqualTo("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Exhibitor is on Applicant")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPage() {
        final ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/events/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(requestBody),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, "b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a");
        assertThat(response.getStatusCode()).as("check if HTTP Status OK").isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody()).as("check if response contains application").and(
                json -> json.node("applicationId").isEqualTo("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a"),
                json -> json.node("userId").isEqualTo(EXHIBITOR_ID_1.toString()),
                json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                json -> json.node("eventTitle").isEqualTo("Test"),
                json -> json.node("exhibitorName").isEqualTo("Hung Luu"),
                json -> json.node("contactInformation").isEqualTo("example@example.com"),
                json -> json.node("businessName").isEqualTo("Sample Business"),
                json -> json.node("message").isEqualTo("Looking forward to participating!"),
                json -> json.node("status").isEqualTo("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Exhibitor is on Applicant but not Found")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPageNotFound() {
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, "b79a8e9b-7d74-4a5f-93e1-73f451e1ae05");
        assertThat(response.getStatusCode()).as("check if HTTP Status OK").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Exhibitor is on Applicant but Not Found")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPageInvalidInput() {
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, null);
        assertThat(response.getStatusCode()).as("check if HTTP Status OK").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Exhibitor is on Applicant but Not Found")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPageInvalidInput2() {
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, UUID.randomUUID());
        assertThat(response.getStatusCode()).as("check if HTTP Status NOT FOUND").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Exhibitor can see his applications")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql" })
    void canSeeHisApplications() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/exhibitor/{exhibitorId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port,
                "af333e19-6ed6-4c8b-b86e-ef78943a98a2");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ApplicationResponse> applications = objectMapper.readValue(
                response.getBody(),
                new TypeReference<List<ApplicationResponse>>() {}
        );

        assertThat(applications).hasSize(2);

        assertThat(applications)
                .extracting(
                        "id",
                        "applicationId",
                        "userId",
                        "eventId",
                        "eventTitle",
                        "exhibitorName",
                        "contactInformation",
                        "businessName",
                        "typeOfBusiness",
                        "previousParticipation",
                        "message",
                        "status"
                )
                .containsExactlyInAnyOrder(
                        tuple(
                                4L,
                                UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2346"),
                                UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"),
                                UUID.fromString("17353065-3834-3030-2d65-3239633d3431"),
                                "Test Event 3",
                                "Khang",
                                "dfadf@example.com",
                                "IT Technik",
                                "Software IT",
                                true,
                                "Looking forward to participating!",
                                "CONTRACT"
                        ),
                        tuple(
                                2L,
                                UUID.fromString("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b"),
                                UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"),
                                UUID.fromString("45353065-3834-3030-2d65-3239633d3431"),
                                "Test",
                                "Khang",
                                "dfadf@example.com",
                                "Luu Consults",
                                "Consulting",
                                true,
                                "Looking forward to participating!",
                                "IN_PROGRESS"
                        )
                );
    }

    @Test
    @DisplayName("Exhibitor cannot see applications when not existing")
    void exhibitorCannotSeeApplications() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/exhibitor/{exhibitorId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port,
                null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("invalid Userid ")
    void invalidUserId() throws Exception {
        UUID invalidUuid = UUID.randomUUID();
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}/exhibitor/{exhibitorId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port,
                invalidUuid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Exhibitor not found for ID: " +invalidUuid.toString());
    }

    @Test
    @DisplayName("Exhibitor has no Applications")
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql" })
    void exhibitorhasNoApplication() throws Exception {
        ResponseEntity<String> responseNoApplications = restTemplate.exchange(
                "http://localhost:{port}/exhibitor/{exhibitorId}/applications",
                HttpMethod.GET,
                null,
                String.class,
                port,
                "bf333e19-6ed6-4c8b-b86e-ef78943a98a4");

        assertThat(responseNoApplications.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ApplicationResponse> applications = objectMapper.readValue(
                responseNoApplications.getBody(),
                new TypeReference<List<ApplicationResponse>>() {}
        );

        assertThat(applications).isEmpty();
    }

}








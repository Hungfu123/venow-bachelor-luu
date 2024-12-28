package org.venow.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.web.request.ApplicationRequest;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class
ExhibitorControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("exhibitor is on event detail page for an event ")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void userGetsOnEventPage() {
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}exhibitor/{userId}/event/{eventId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, EXHIBITOR_ID_1, EVENT_ID_1);
        assertThat(response.getStatusCode())
                .as("check if HTTP Status 200 OK")
                .isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody())
                .as("check if response contains expected event details")
                .and(
                        json -> json.node("eventId").isEqualTo(EVENT_1.getEventId().toString()),
                        json -> json.node("title").isEqualTo(EVENT_1.getTitle()),
                        json -> json.node("description").isEqualTo(EVENT_1.getDescription()),
                        json -> json.node("location").isEqualTo(EVENT_1.getLocation()),
                        json -> json.node("startDate").isEqualTo(EVENT_1.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        json -> json.node("endDate").isEqualTo(EVENT_1.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );

    }

    @Test
    @DisplayName("exhibitor is on event detail page and gets NotFound ")
    void userGetsBadRequest() {
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}exhibitor/{userId}/event/{eventId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, EXHIBITOR_ID_1, null);
        assertThat(response.getStatusCode())
                .as("check if HTTP Status NOT FOUND")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should return all 2 events")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    public void test() {
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "http://localhost:{port}exhibitor/{exhibitorId}/event",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, USER_EXHIBITOR_3.getUserId());

        assertThat(response.getStatusCode()).as("Checking for status code").isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody())
                .as("Checking for events")
                .isArray()
                .hasSize(2)
                .extracting("eventId", "title")
                .contains(
                        tuple("45353065-3834-3030-2d65-3239633d3431", "Test"),
                        tuple("35353065-3834-3030-2d65-3239622d3432", "Thiens Schmackofatz")
                );
    }

    @Test
    @DisplayName("exhibitor gets on application for eventId")
    void applicationGetsOnApplicationPage() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{exhibitorId}/event/{eventId}/application",
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void exhibitorApplyForEventPage() {

        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{userId}/event/{eventId}/application",
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
                        json -> json.node("contactInformation").isEqualTo("example@example.com"),
                        json -> json.node("businessName").isEqualTo("Sample Business"),
                        json -> json.node("status").isEqualTo("IN_PROGRESS")
                );
    }

    @Test
    @DisplayName("exhibitor apply for the same event again - CONFLICTED")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void exhibitorApplyForSameEventAgain() {

        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{userId}/event/{eventId}/application",
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void anotherExhibitorApplyForSameEvent() {

        ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application",
                        HttpMethod.POST,
                new HttpEntity<>(requestBody),
                String.class,
                port, USER_EXHIBITOR_3.getUserId(), EVENT_ID_1);

        assertThat(response.getStatusCode()).as("check if HTTP Status 201 CREATED").isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody()).as("check if response contains application").and(
                json -> json.node("applicationId").isNotNull(),
                        json -> json.node("userId").isEqualTo(EXHIBITOR_ID_2.toString()),
                        json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                        json -> json.node("contactInformation").isEqualTo("example@example.com"),
                        json -> json.node("businessName").isEqualTo("Sample Business"),
                        json -> json.node("status").isEqualTo("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Exhibitor is on Applicant")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPage() {
        final ApplicationRequest requestBody = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(requestBody),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, "b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a");
        assertThat(response.getStatusCode()).as("check if HTTP Status OK").isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody()).as("check if response contains application").and(
                json -> json.node("applicationId").isEqualTo("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a"),
                json -> json.node("userId").isEqualTo(EXHIBITOR_ID_1.toString()),
                json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                json -> json.node("contactInformation").isEqualTo("example@example.com"),
                json -> json.node("businessName").isEqualTo("Sample Business"),
                json -> json.node("message").isEqualTo("Looking forward to participating!"),
                json -> json.node("status").isEqualTo("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Exhibitor is on Applicant but not Found")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void onApplicationDetailPageInvalidInput2() {
        final ResponseEntity<String> response = restTemplate.exchange("http://localhost:{port}exhibitor/{userId}/event/{eventId}/application/{applicationId}",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4", EVENT_ID_1, UUID.randomUUID());
        assertThat(response.getStatusCode()).as("check if HTTP Status NOT FOUND").isEqualTo(HttpStatus.NOT_FOUND);
    }

}






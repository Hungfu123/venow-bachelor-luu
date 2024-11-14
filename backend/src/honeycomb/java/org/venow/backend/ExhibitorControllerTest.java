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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class ExhibitorControllerTest extends TestBase{

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
                        port, EXHIBITOR_ID_1,EVENT_ID_1);
    assertThat(response.getStatusCode())
            .as("check if HTTP Status 200 OK")
            .isEqualTo(HttpStatus.OK);

        String jsonBody = response.getBody();
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
    @DisplayName("exhibitor gets on application for eventId")
    void applicationGetsOnApplicationPage() {
        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}exhibitor/{userId}/event/{eventId}/application",
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


}






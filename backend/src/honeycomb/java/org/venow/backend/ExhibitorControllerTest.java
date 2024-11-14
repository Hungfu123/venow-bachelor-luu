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
        assertThatJson(jsonBody).as("check eventId").node("eventId").isEqualTo(EVENT_1.getEventId().toString());
        assertThatJson(jsonBody).as("check title").node("title").isEqualTo(EVENT_1.getTitle());
        assertThatJson(jsonBody).as("check description").node("description").isEqualTo(EVENT_1.getDescription());
        assertThatJson(jsonBody).as("check location").node("location").isEqualTo(EVENT_1.getLocation());
        assertThatJson(jsonBody).as("check startDate")
                .node("startDate").isEqualTo(EVENT_1.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThatJson(jsonBody).as("check endDate")
                .node("endDate").isEqualTo(EVENT_1.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

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
    }
}





package org.venow.backend;


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

import java.time.format.DateTimeFormatter;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class OrganizerEventControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("should return all events by organizer")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldReturnAllEventsByOrganizer() {
        final ResponseEntity<String> response =
                restTemplate.exchange(
                "http://localhost:{port}/organizer/{organizerId}/events",
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class,
                port, USER_ORGANIZER_1.getUserId());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody())
                .as("Checking for events")
                .isArray()
                .hasSize(2)
                .extracting("eventId", "title")
                .contains(
                        tuple("17353065-3834-3030-2d65-3239633d3431", "Test Event 3"),
                        tuple("17353065-3834-3030-2d65-3239633d3432", "Test Event 4")
                );
    }

    @Test
    @DisplayName("organizer is on event detail page for an event ")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void userGetsOnEventPage() {
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/{eventId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, "123e4567-e89b-12d3-a456-426614174000", "45353065-3834-3030-2d65-3239633d3431");
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
                        json -> json.node("organizerName").isEqualTo("Robert J"),
                        json -> json.node("endDate").isEqualTo(EVENT_1.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );

    }

    @Test
    @DisplayName("organizer is on event detail page and gets NotFound ")
    void userGetsBadRequest() {
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}organizer/{userId}/events/{eventId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, EXHIBITOR_ID_1, null);
        assertThat(response.getStatusCode())
                .as("check if HTTP Status NOT FOUND")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }



}

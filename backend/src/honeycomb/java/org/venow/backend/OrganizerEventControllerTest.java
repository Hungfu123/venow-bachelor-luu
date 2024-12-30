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
import org.venow.backend.domain.model.User;
import org.venow.backend.infrastructure.EventRepositoryImpl;
import org.venow.backend.testfactory.UserTestData;
import org.venow.backend.web.request.EventRequest;

import java.time.format.DateTimeFormatter;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class OrganizerEventControllerTest extends TestBase {
    @Test
    @DisplayName("organizer edit event")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerEditEvent() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/edit/{eventId}",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId(), "45353065-3834-3030-2d65-3239633d3431");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 200 OK")
                .isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody())
                .as("check if response contains expected event details for created event")
                .and(
                        json -> json.node("title").isEqualTo("Test Event"),
                        json -> json.node("description").isEqualTo("Test Description"),
                        json -> json.node("location").isEqualTo("Test Location"),
                        json -> json.node("startDate").isEqualTo("2021-12-12T12:00:00"),
                        json -> json.node("endDate").isEqualTo("2021-12-12T14:00:00"),
                        json -> json.node("organizerName").isEqualTo("Robert J")
                );
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventRepositoryImpl eventrepository;

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

    @Test
    @DisplayName("organizer create event")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerCreateEvent() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/create",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId());

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 201 CREATED")
                .isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody())
                .as("check if response contains expected event details for created event")
                .and(
                        json -> json.node("title").isEqualTo("Test Event"),
                        json -> json.node("description").isEqualTo("Test Description"),
                        json -> json.node("location").isEqualTo("Test Location"),
                        json -> json.node("startDate").isEqualTo("2021-12-12T12:00:00"),
                        json -> json.node("endDate").isEqualTo("2021-12-12T14:00:00"),
                        json -> json.node("organizerName").isEqualTo("Robert J")
                );
    }

    @Test
    @DisplayName("organizer create event with empty validation")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerCreateEventWithEmpty() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request-empty.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/create",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId());

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 400 BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatJson(
                response.getBody()
        ).as("check if response contains expected error message")
                .and(
                        json -> json.node("title").isEqualTo("darf nicht leer sein"),
                        json -> json.node("description").isEqualTo("darf nicht leer sein"),
                        json -> json.node("description").isEqualTo("darf nicht leer sein"),
                        json -> json.node("startDate").isEqualTo("darf nicht null sein"),
                        json -> json.node("endDate").isEqualTo("darf nicht null sein")
                );

    }

    @Test
    @DisplayName("organizer create event with invalid date")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerCreateEventWithInvalidDate() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request-invalid-date.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/create",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId());

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 400 BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatJson(
                response.getBody()
        ).as("check if response contains expected error message")
                .and(
                        json -> json.node("message").isEqualTo("End date must be after start date")
                );

    }

    @Test
    @DisplayName("organizer edit event with invalid date")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerEditEventWithInvalidDate() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request-invalid-date.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/edit/{eventId}",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId(), "45353065-3834-3030-2d65-3239633d3431");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 400 BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatJson(
                response.getBody()
        ).as("check if response contains expected error message")
                .and(
                        json -> json.node("message").isEqualTo("End date must be after start date")
                );

    }

    @Test
    @DisplayName("organizer edit event with empty validation")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void organizerEditEventWithEmpty() throws Exception {
        final EventRequest eventRequest = loadObjectFromJson("/requests/event-request-empty.json", EventRequest.class);
        final User user = UserTestData.createOrganizer1();
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}/organizer/{organizerId}/events/edit/{eventId}",
                        HttpMethod.POST,
                        new HttpEntity<>(eventRequest),
                        String.class,
                        port, user.getUserId(), "45353065-3834-3030-2d65-3239633d3431");

        assertThat(response.getStatusCode())
                .as("check if HTTP Status 400 BAD REQUEST")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatJson(
                response.getBody()
        ).as("check if response contains expected error message")
                .and(
                        json -> json.node("title").isEqualTo("darf nicht leer sein"),
                        json -> json.node("description").isEqualTo("darf nicht leer sein"),
                        json -> json.node("description").isEqualTo("darf nicht leer sein"),
                        json -> json.node("startDate").isEqualTo("darf nicht null sein"),
                        json -> json.node("endDate").isEqualTo("darf nicht null sein")
                );

    }

}

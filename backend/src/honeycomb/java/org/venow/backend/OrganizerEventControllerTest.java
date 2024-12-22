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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class OrganizerEventControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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


}

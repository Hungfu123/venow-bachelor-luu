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

import java.time.format.DateTimeFormatter;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class ExhibitorControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("user is on event detail page for an event ")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    void userGetsOnEventPage() {
        final ResponseEntity<String> response = restTemplate
                .exchange(
                        "http://localhost:{port}exhibitor/{userId}/event/{eventId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, USER_EXHIBITOR_1.getUserId(),EVENT_1.getEventId());
    assertThat(response.getStatusCode())
            .as("check if HTTP Status 200 OK")
            .isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody()).as("check eventId").node("eventId").isEqualTo(EVENT_1.getEventId().toString());
        assertThatJson(response.getBody()).as("check title").node("title").isEqualTo(EVENT_1.getTitle());
        assertThatJson(response.getBody()).as("check description").node("description").isEqualTo(EVENT_1.getDescription());
        assertThatJson(response.getBody()).as("check location").node("location").isEqualTo(EVENT_1.getLocation());
        assertThatJson(response.getBody()).as("check startDate")
                .node("startDate").isEqualTo(EVENT_1.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThatJson(response.getBody()).as("check endDate")
                .node("endDate").isEqualTo(EVENT_1.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));


    }
    }





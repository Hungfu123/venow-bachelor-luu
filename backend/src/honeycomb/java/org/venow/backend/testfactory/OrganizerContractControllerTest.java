package org.venow.backend.testfactory;

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
import org.venow.backend.TestBase;

import java.time.LocalDateTime;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizerContractControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get on contract formular page und see contract response")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void getFormularContract() {

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_2.applicationId());

        assertThat(response.getStatusCode())
                .as("check if HTTP is OK")
                .isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody()).as("check if contract response is correct").and(
                jsonNode -> jsonNode.node("contractId").isNotNull(),
                jsonNode -> jsonNode.node("organizerId").isEqualTo(ORGANIZER_ID.toString()),
                jsonNode -> jsonNode.node("applicantName").isEqualTo("Hung Luu"),
                jsonNode -> jsonNode.node("organizerName").isEqualTo("Frank Stein"),
                jsonNode -> jsonNode.node("eventName").isEqualTo("Test"),
                jsonNode -> jsonNode.node("location").isEqualTo("Test Park"),
                jsonNode -> jsonNode.node("startDate").isEqualTo("2024-06-01T12:00:00"),
                jsonNode -> jsonNode.node("endDate").isEqualTo("2024-06-01T23:00:00"),
                jsonNode -> jsonNode.node("typeOfBusiness").isEqualTo("Food Truck"),
                jsonNode -> jsonNode.node("contactInformation").isEqualTo("example@example.com"),
                jsonNode -> jsonNode.node("businessName").isEqualTo("Sample Business"),
                jsonNode -> jsonNode.node("terms").isEqualTo("By accepting this contract, you agree to the terms and conditions of the event."),
                jsonNode -> jsonNode.node("price").isEqualTo(" null"),
                jsonNode -> jsonNode.node("status").isEqualTo("IN_PROGRESS"),
                jsonNode -> jsonNode.node("organizerEmail").isEqualTo("organizer1@example.com")
        );
    }

    @DisplayName("not found contract")
    @Test
    void notFoundContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID, UUID.randomUUID());

        assertThat(response.getStatusCode())
                .as("check if HTTP is NOT_FOUND")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("not found contract null")
    @Test
    void notfoundContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID, null);

        assertThat(response.getStatusCode())
                .as("check if HTTP is NOT_FOUND")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }


}

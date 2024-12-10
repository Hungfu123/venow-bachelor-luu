package org.venow.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.venow.backend.testfactory.ApplicationDataFactory;
import org.venow.backend.web.reponse.ApplicationResponse;

import java.util.List;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class OrganizerApplicationControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get all Applications by an organizer")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
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
                .containsExactlyInAnyOrderElementsOf(expectedRespones);

    }

    @DisplayName("Organizer has no applications")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
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
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void rejectApplication() throws JsonProcessingException {

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:{port}organizer/{organizerId}/applications/{applicationId}",
                HttpMethod.POST,
                null,
                String.class,
                port, ORGANIZER_ID.toString(), APPLICATION_1.applicationId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ApplicationResponse actualResponses = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {
                }
        );

        assertThatJson(response.getBody()).as("check if application contains status rejected").and(
                json -> json.node("applicationId").isEqualTo(APPLICATION_1.applicationId().toString()),
                json -> json.node("userId").isEqualTo("af333e19-6ed6-4c8b-b86e-ef78943a98a3"),
                json -> json.node("eventId").isEqualTo(EVENT_ID_1.toString()),
                json -> json.node("contactInformation").isEqualTo("dfadf@example.com"),
                json -> json.node("businessName").isEqualTo("IT Technik"),
                json -> json.node("status").isEqualTo("REJECTED"));
    }

}

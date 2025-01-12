package org.venow.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.testfactory.ContractDataFactory;
import org.venow.backend.web.reponse.ContractResponse;
import org.venow.backend.web.request.ContractRequest;

import java.util.List;
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
                jsonNode -> jsonNode.node("price").isEqualTo(" 0.0"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo(" null"),
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

    @DisplayName("submit contract formular and create contract")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void submitContractAndCreateContract() {
        ContractRequest request = loadObjectFromJson("/requests/contract-request.json", ContractRequest.class);

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_2.applicationId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThatJson(response.getBody()).and(
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
                jsonNode -> jsonNode.node("price").isEqualTo("50.0"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("A50"),
                jsonNode -> jsonNode.node("status").isEqualTo("CONTRACT"),
                jsonNode -> jsonNode.node("organizerEmail").isEqualTo("organizer1@example.com")
        );
    }

    @DisplayName("submitWithoutRequest - BAD REQUEST")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void submitWithoutRequest() {

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.POST,
                        new HttpEntity<>(null),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_2.applicationId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @DisplayName("submit with validation - BAD REQUEST")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void submitWithValidation() {
        ContractRequest request = loadObjectFromJson("/requests/contract-request-null.json", ContractRequest.class);

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_2.applicationId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = response.getBody();
        assertThatJson(responseBody).and(
                jsonNode -> jsonNode.node("price").isEqualTo("Price must not be null"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("Stand number must not be empty")
        );
    }

    @DisplayName("submit with validation empty booth number and price must be greater than 0 - BAD REQUEST")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void subWIthValidationEmpty() {
        ContractRequest request = loadObjectFromJson("/requests/contract-request-empty.json", ContractRequest.class);

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_2.applicationId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = response.getBody();
        assertThatJson(responseBody)
                .node("boothNumber").isEqualTo("Stand number must not be empty");

        assertThatJson(responseBody)
                .node("price.amountInEuro").isEqualTo("Price must be greater than 0");
    }

    @DisplayName("submit on not in progress status application - CONFLICT")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void submitOnNotInProgressStatusApplication() {
        ContractRequest request = loadObjectFromJson("/requests/contract-request.json", ContractRequest.class);

        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/{applicationId}",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        String.class,
                        port, ORGANIZER_ID, APPLICATION_3.applicationId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        assertThat(response.getBody()).isEqualTo("Application Status must be in progress");
    }

    @DisplayName("show all contracts by organizer")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application-contracts-first.sql"})
    void showAllContractsByOrganizer() throws JsonProcessingException {
        List<ContractResponse> expectedContracts = ContractDataFactory.getAllContractResponses();


        final ResponseEntity<List<ContractResponse>> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ContractResponse>>() {},
                        port,
                        "b23e4567-e89b-12d3-a456-426614174002");


        assertThat(response.getBody())
                .as("check if response contains expected contracts by organizerId")
                .hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("price.amountInEuro")
                .isEqualTo(expectedContracts);

        assertThat(response.getBody().getFirst().price().getAmountInEuro().doubleValue()).isEqualTo(3.0);
        assertThat(response.getBody().getLast().price().getAmountInEuro().doubleValue()).isEqualTo(3.0);
    }

    @DisplayName("cant show contracts because user is EXHIBITOR - NOT FOUND")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    void showNotFoundExhibitor() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, "df333e19-6ed6-4c8b-b86e-ef78943a98a4");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isEqualTo("Organizer ID not found");
    }

    @DisplayName("organizer see contract details")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    void showContractDetails() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, "b23e4567-e89b-12d3-a456-426614174002", "9a330d97-f4fb-4596-ab33-b826a0919272");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody()).and(
                jsonNode -> jsonNode.node("contractId").isEqualTo("9a330d97-f4fb-4596-ab33-b826a0919272"),
                jsonNode -> jsonNode.node("organizerId").isEqualTo("b23e4567-e89b-12d3-a456-426614174002"),
                jsonNode -> jsonNode.node("applicantName").isEqualTo("Khang"),
                jsonNode -> jsonNode.node("organizerName").isEqualTo("Robert J"),
                jsonNode -> jsonNode.node("eventName").isEqualTo("Test Event 3"),
                jsonNode -> jsonNode.node("location").isEqualTo("Mühlheim"),
                jsonNode -> jsonNode.node("startDate").isEqualTo("2024-06-01T12:00:00"),
                jsonNode -> jsonNode.node("endDate").isEqualTo("2024-06-01T23:00:00"),
                jsonNode -> jsonNode.node("typeOfBusiness").isEqualTo("Software IT"),
                jsonNode -> jsonNode.node("contactInformation").isEqualTo("dfadf@example.com"),
                jsonNode -> jsonNode.node("businessName").isEqualTo("IT Technik"),
                jsonNode -> jsonNode.node("terms").isEqualTo("Basic Terms"),
                jsonNode -> jsonNode.node("price").isEqualTo("3.0"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("A10"),
                jsonNode -> jsonNode.node("status").isEqualTo("CONTRACT"),
                jsonNode -> jsonNode.node("organizerEmail").isEqualTo("organizer3@example.com")
        );
    }

    @DisplayName("cant see contract details because contract not found - NOT FOUND")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    void showContractDetailsNotFound() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/organizer/{organizerId}/contracts/details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port, "b23e4567-e89b-12d3-a456-426614174002", UUID.randomUUID());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

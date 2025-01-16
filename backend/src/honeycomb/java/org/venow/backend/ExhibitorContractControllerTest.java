package org.venow.backend;

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
import org.venow.backend.service.ContractRepository;
import org.venow.backend.testfactory.ContractDataFactory;
import org.venow.backend.web.reponse.ContractResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ExhibitorContractControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("exhibitor get on contract details")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void getContractDetails() {
        final ResponseEntity<ContractResponse> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        ContractResponse.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2"
                        ,"c3456789-01cd-ef23-4567-8901cdef2346",
                        "9a330d97-f4fb-4596-ab33-b826a0919272");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ContractResponse contractResponse = response.getBody();
        assert contractResponse != null;
        assertThat(contractResponse.contractId()).isEqualTo(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"));
        assertThat(contractResponse.organizerId()).isEqualTo(UUID.fromString("b23e4567-e89b-12d3-a456-426614174002"));
        assertThat(contractResponse.exhibitorId()).isEqualTo(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"));
        assertThat(contractResponse.applicationId()).isEqualTo(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2346"));
        assertThat(contractResponse.applicantName()).isEqualTo("Khang");
        assertThat(contractResponse.organizerName()).isEqualTo("Robert J");
        assertThat(contractResponse.eventName()).isEqualTo("Test Event 3");
        assertThat(contractResponse.location()).isEqualTo("Mühlheim");
        assertThat(contractResponse.startDate()).isEqualTo(LocalDateTime.parse("2024-06-01T12:00:00"));
        assertThat(contractResponse.endDate()).isEqualTo(LocalDateTime.parse("2024-06-01T23:00:00"));
        assertThat(contractResponse.typeOfBusiness()).isEqualTo("Software IT");
        assertThat(contractResponse.contactInformation()).isEqualTo("dfadf@example.com");
        assertThat(contractResponse.businessName()).isEqualTo("IT Technik");
        assertThat(contractResponse.terms()).isEqualTo("Basic Terms");
        assertThat(contractResponse.price().getAmountInEuro()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(contractResponse.boothNumber()).isEqualTo("A10");
        assertThat(contractResponse.status()).isEqualTo("CONTRACT");
        assertThat(contractResponse.organizerEmail()).isEqualTo("organizer3@example.com");
    }

    @DisplayName("exhibitor get on contract details - invalid applicationId")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void getContractDetailsInvalidApplicationId() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        null,
                        "9a330d97-f4fb-4596-ab33-b826a0919272");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Not Found");
    }

    @DisplayName("exhibitor get on contract details - mismatched IDs")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void getContractDetailsMismatchedIds() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "c3456789-01cd-ef23-4567-8901cdef2346",
                        "9a330d97-f4fb-4596-ab33-b826a0919271");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Contract not found for ID: " + "9a330d97-f4fb-4596-ab33-b826a0919271");
    }

    @DisplayName("exhibitor confirm contract")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void confirmContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/confirm",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9a330d97-f4fb-4596-ab33-b826a0919272");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        contractRepository.findById(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"))
                .ifPresent(contractDTO -> assertThat(contractDTO.getStatus()).isEqualTo("CONFIRMED"));

    }

    @DisplayName("exhibitor cant find contract - invalid status")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void confirmContractInvalidStatus() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/confirm",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9a330d97-f4fb-4596-ab33-b826a0919271");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Contract not found for ID: " + "9a330d97-f4fb-4596-ab33-b826a0919271");
    }

    @DisplayName("contracted already accepted - invalid status")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void confirmContractInvalidStatusNotContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/confirm",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "551f8d69-996d-42c7-8b04-cd9e4ac6aa06");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("Contract status must be CONTRACT");

        final ResponseEntity<String> rejectedResponse = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/confirm",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9452c6a7-6acc-4208-bbb3-f04396326fc2");

        assertThat(rejectedResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rejectedResponse.getBody()).contains("Contract status must be CONTRACT");
    }

    @DisplayName("exhibitor reject contract")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void rejectContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/reject",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9a330d97-f4fb-4596-ab33-b826a0919272");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        contractRepository.findById(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"))
                .ifPresent(contractDTO -> assertThat(contractDTO.getStatus()).isEqualTo("REJECTED"));

    }

    @DisplayName("exhibitor cant find contract - invalid status")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void rejectContractInvalidStatus() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/reject",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9a330d97-f4fb-4596-ab33-b826a0919271");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Contract not found for ID: " + "9a330d97-f4fb-4596-ab33-b826a0919271");
    }

    @DisplayName("contract already rejected and accepted - invalid status")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void rejectContractInvalidStatusNotContract() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/reject",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "551f8d69-996d-42c7-8b04-cd9e4ac6aa06");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("Contract status must be CONTRACT");

        final ResponseEntity<String> rejectedResponse = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}/reject",
                        HttpMethod.PUT,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9452c6a7-6acc-4208-bbb3-f04396326fc2");

        assertThat(rejectedResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rejectedResponse.getBody()).contains("Contract status must be CONTRACT");
    }

    @DisplayName("exhibitor get on contract details")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void getContractDetailsFromAll() {
        final ResponseEntity<ContractResponse> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contracts/details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        ContractResponse.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "9a330d97-f4fb-4596-ab33-b826a0919272");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ContractResponse contractResponse = response.getBody();
        assert contractResponse != null;
        assertThat(contractResponse.contractId()).isEqualTo(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"));
        assertThat(contractResponse.organizerId()).isEqualTo(UUID.fromString("b23e4567-e89b-12d3-a456-426614174002"));
        assertThat(contractResponse.exhibitorId()).isEqualTo(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"));
        assertThat(contractResponse.applicationId()).isEqualTo(UUID.fromString("c3456789-01cd-ef23-4567-8901cdef2346"));
        assertThat(contractResponse.applicantName()).isEqualTo("Khang");
        assertThat(contractResponse.organizerName()).isEqualTo("Robert J");
        assertThat(contractResponse.eventName()).isEqualTo("Test Event 3");
        assertThat(contractResponse.location()).isEqualTo("Mühlheim");
        assertThat(contractResponse.startDate()).isEqualTo(LocalDateTime.parse("2024-06-01T12:00:00"));
        assertThat(contractResponse.endDate()).isEqualTo(LocalDateTime.parse("2024-06-01T23:00:00"));
        assertThat(contractResponse.typeOfBusiness()).isEqualTo("Software IT");
        assertThat(contractResponse.contactInformation()).isEqualTo("dfadf@example.com");
        assertThat(contractResponse.businessName()).isEqualTo("IT Technik");
        assertThat(contractResponse.terms()).isEqualTo("Basic Terms");
        assertThat(contractResponse.price().getAmountInEuro()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(contractResponse.boothNumber()).isEqualTo("A10");
        assertThat(contractResponse.status()).isEqualTo("CONTRACT");
        assertThat(contractResponse.organizerEmail()).isEqualTo("organizer3@example.com");
    }

    @DisplayName("exhibitor get on contract details - invalid applicationId")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application-contracts.sql"})
    public void getContractDetailsInvalidContractId() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contract-details/{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2",
                        "null");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Not Found");
    }

    @DisplayName("exhibitor get all contracts")
    @Test
    @Sql(scripts = { "/db/testmigration/005_insert-event-user-application-contracts-exhibitor.sql"})
    public void getAllContracts() {
        List<ContractResponse> expectedContracts = ContractDataFactory.getAllContractResponseExhibitor();

        final ResponseEntity<List<ContractResponse>> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contracts",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<ContractResponse>>() {},
                        port,
                        "af333e19-6ed6-4c8b-b86e-ef78943a98a2");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .as("check if response contains expected contracts by exhibitorId")
                .hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("price.amountInEuro")
                .isEqualTo(expectedContracts);

        assertThat(response.getBody().getFirst().price().getAmountInEuro().doubleValue()).isEqualTo(3.0);
        assertThat(response.getBody().getLast().price().getAmountInEuro().doubleValue()).isEqualTo(3.0);
    }


}

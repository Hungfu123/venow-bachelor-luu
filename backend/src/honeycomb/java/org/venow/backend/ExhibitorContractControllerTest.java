package org.venow.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ExhibitorContractControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("exhibitor get on contract details")
    @Test
    public void getContractDetails() {
        final ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:{port}/exhibitor/{exhibitorId}/contracts/details{contractId}",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port,"123e4567-e89b-12d3-a456-426614174000","45353065-3834-3030-2d65-3239633d3431");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }



}

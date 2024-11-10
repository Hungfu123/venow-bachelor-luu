package org.venow.backend;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EventControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("should return all events")
    public void test() {
        ResponseEntity<String> response =
                restTemplate.exchange("http://localhost:" + port + "/events",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class);

        Assertions.assertThat(response.getStatusCode()).as("Checking for status code").isEqualTo(HttpStatus.OK);
    }
}

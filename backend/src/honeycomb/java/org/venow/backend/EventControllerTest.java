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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

 class EventControllerTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("should return all 2 events")
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/000_init.sql"})
    public void test() {
        ResponseEntity<String> response =
                restTemplate.exchange("http://localhost:{port}/events",
                        HttpMethod.GET,
                        new HttpEntity<>(null),
                        String.class,
                        port);

        assertThat(response.getStatusCode()).as("Checking for status code").isEqualTo(HttpStatus.OK);

        assertThatJson(response.getBody())
                .as("Checking for events")
                .isArray()
                .hasSize(2)
                .extracting("eventId", "title")
                .contains(
                        tuple("45353065-3834-3030-2d65-3239633d3431", "Test"),
                        tuple("35353065-3834-3030-2d65-3239622d3432", "Thiens Schmackofatz")
                );
    }
}

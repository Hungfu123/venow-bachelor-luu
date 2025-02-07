package org.venow.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should return 401 when unauthenticated")
    void testHealthUnauthenticated() {
        final ResponseEntity<String> response = restTemplate.getForEntity("/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

//    @Test
//    @DisplayName("Should return 200 when authenticated")
//    @Sql(scripts = { "/db/testmigration/000_init.sql"})
//    void testHealthAuthenticated() {
//        final ResponseEntity<String> response = restTemplate.getForEntity("/health", String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
}

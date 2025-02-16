package org.venow.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
public class AuthTest extends TestBase {


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should return 302 when coming to health")
    void testHealthUnauthenticated() {
        final ResponseEntity<String> response = restTemplate.getForEntity("/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    @DisplayName("Should return 200 when authenticated")
    @Sql(scripts = {"/db/testmigration/008_security.sql"})
    void testHealthAuthenticated() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("organizer3@example.com", "password123");

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/health", HttpMethod.GET, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should login successfully and redirect as organizer")
    @Sql(scripts = {"/db/testmigration/008_security.sql"})
    void testLoginSuccessOrganizer() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "organizer3@example.com");
        formData.add("password", "password123");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaders().getLocation().toString()).contains("/organizer/123e4567-e89b-12d3-a456-426614174000/events");

    }

    @Test
    @DisplayName("Should login successfully and redirect as exhibitor")
    @Sql(scripts = {"/db/testmigration/008_security.sql"})
    void testLoginSuccessExhibitor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "showman@example.com");
        formData.add("password", "password123");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaders().getLocation().toString()).contains("/exhibitor/df333e19-6ed6-4c8b-b86e-ef78943a98a4/events");

    }

    @Test
    @DisplayName("Should login not successfully  as exhibitor")
    @Sql(scripts = {"/db/testmigration/008_security.sql"})
    void testLoginSuccessExhibitorUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "showman@example2.com");
        formData.add("password", "password123");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaders().getLocation().toString()).contains("/login?error");

    }


}

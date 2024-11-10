package org.venow.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

class ShowmannControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("user apply for an event ")
    void applyForEvent() {
        ResponseEntity<String> response = restTemplate.postForEntity("showman/{id}/event/{eventid}", null, String.class);
    }
}

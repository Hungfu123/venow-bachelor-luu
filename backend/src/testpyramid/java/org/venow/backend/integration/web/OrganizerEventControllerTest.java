package org.venow.backend.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.OrganizerEventController;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizerEventController.class)
public class OrganizerEventControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Eventservice eventService;

    @Test
    @DisplayName("should return all 2 events")
    public void test() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerId(testuser.userId()).build();
        final EventDTO expectedEvent2 = EventDTOFactory.createDefault().withEventId(UUID.randomUUID()).withOrganizerId(testuser.userId()).build();

        when(eventService.getAllEventsByOrganizer(testuser.userId(), Role.ORGANIZER)).thenReturn(List.of(expectedEvent, expectedEvent2));

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/events", testuser.userId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EventDTO> eventDTOS = objectMapper.readValue(contentAsString, List.class);
        assertThat(eventDTOS).hasSize(2);

    }

    @Test
    @DisplayName("cant find organizerId")
    public void test2() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();

        when(eventService.getAllEventsByOrganizer(testuser.userId(), Role.ORGANIZER))
                .thenThrow(new NotFoundException("OrganizerId not found"));

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/events", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("OrganizerId not found");

    }
}

package org.venow.backend.integration.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.service.*;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.exhibitor.ExhibitorController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExhibitorController.class)
class ExhibitorEventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    Eventservice eventservice;

    @MockBean
    ApplicationService applicationservice;


    @DisplayName("get specific Event")
    @Test
    void getEventId() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}", testuser.userId(), expectedEvent.eventId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(eventservice, times(1)).getEvent(any());
    }

    @DisplayName("get all Events by searching title")
    @Test
    void getAllEventsBySearchTitle() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        final EventDTO expectedEvent2 = EventDTOFactory.createDefault().withTitle("Ok").build();
        when(eventservice.getEventsBySearchTitle(any())).thenReturn(List.of(expectedEvent, expectedEvent2));

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events?query={query}", testuser.userId(), "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").exists())
                .andExpect(jsonPath("$[0].title").value("Test Event"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].title").value("Ok"))
                .andExpect(jsonPath("$[1].description").value("Test Description"));

        verify(eventservice, times(1)).getEventsBySearchTitle(any());
    }

    @DisplayName("get all Events by not giving one letter in query")
    @Test
    void getAllEvents() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        when(eventservice.getEventsBySearchTitle(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events?query={query}", testuser.userId(), "T"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @DisplayName("get all Events by not giving two letter in query")
    @Test
    void getAllEvents2() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        when(eventservice.getEventsBySearchTitle(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events?query={query}", testuser.userId(), "Te"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @DisplayName("get all Events by not gicing query")
    @Test
    void getAllEventsByQuery() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        final EventDTO expectedEvent2 = EventDTOFactory.createDefault().withTitle("Ok").build();
        when(eventservice.getEventsBySearchTitle(any())).thenReturn(List.of(expectedEvent, expectedEvent2));

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events?query={query}", testuser.userId(), "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").exists())
                .andExpect(jsonPath("$[0].title").value("Test Event"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].title").value("Ok"))
                .andExpect(jsonPath("$[1].description").value("Test Description"));

        verify(eventservice, times(1)).getEventsBySearchTitle(any());
    }

    @DisplayName("cant get specific Event - BadRequest")
    @Test
    void cantGetEventId() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}", null, UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }



}

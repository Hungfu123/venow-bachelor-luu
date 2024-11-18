package org.venow.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.applicationservice.*;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.request.ApplicationRequest;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.venow.backend.TestBase.loadObjectFromJson;

@WebMvcTest(ExhibitorController.class)
class ExhibitorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    Eventservice eventservice;
    @MockBean
    Applicationservice applicationservice;

    @DisplayName("get specific Event")
    @Test
    void getEventId() throws Exception {
        final UserDTO testuser = UserDTOFactory.createWithExhibitorRole();
        final EventDTO expectedEvent = EventDTOFactory.createDefault();
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}", testuser.userId(), expectedEvent.eventId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(eventservice, times(1)).getEvent(any());
    }

    @DisplayName("cant get specific Event - BadRequest")
    @Test
    void cantGetEventId() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}", null, UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }

    @DisplayName("get Application Details")
    @Test
    void exhibitorGetApplicationDetails() throws Exception {
        final UserDTO testuser = UserDTOFactory.createWithExhibitorRole();
        final EventDTO expectedEvent = EventDTOFactory.createDefault();
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}/application", testuser.userId(), expectedEvent.eventId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(""));

    }

    @DisplayName("cant get Application Details on even - Not Found")
    @Test
    void exhibitorCantGetApplicationDetails() throws Exception {
        final EventDTO expectedEvent = EventDTOFactory.createDefault();

        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}/application", null, expectedEvent.eventId()))
                .andExpect(status().isNotFound());
    }

    @DisplayName(" apply for an event")
    @Test
    void exhibitorApplyForAnEvent() throws Exception {

        ApplicationRequest applicationRequest = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final UserDTO testuser = UserDTOFactory.createWithExhibitorRole();
        final EventDTO expectedEvent = EventDTOFactory.createDefault();

        final ApplicationDTO expectedApplicationDTO = ApplicationDTOFactory.createDefault();
        when(applicationservice.createApplication(any())).thenReturn(expectedApplicationDTO);

        mockMvc.perform(post("/exhibitor/{exhibitorId}/event/{eventId}/application", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(applicationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value(expectedApplicationDTO.applicationId().toString()))
                .andExpect(jsonPath("$.eventId").value(expectedApplicationDTO.eventId().toString()))
                .andExpect(jsonPath("$.userId").value(expectedApplicationDTO.userId().toString()))
                .andExpect(jsonPath("$.businessName").value(expectedApplicationDTO.businessName().toString()))
                .andExpect(jsonPath("$.status").value(expectedApplicationDTO.status().toString()));
    }

    @DisplayName("apply for an event without applicationinformation - bad request")
    @Test
    void exhibitorApplyForAnEventWithoutApplicationInformation() throws Exception {
        final UserDTO testuser = UserDTOFactory.createWithExhibitorRole();
        final EventDTO expectedEvent = EventDTOFactory.createDefault();

        mockMvc.perform(post("/exhibitor/{exhibitorId}/event/{eventId}/application", testuser.userId(), expectedEvent.eventId()))
                .andExpect(status().isBadRequest());
    }

}

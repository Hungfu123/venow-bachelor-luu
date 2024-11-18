package org.venow.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.Applicationservice;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.Eventservice;
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

    @DisplayName("exhibitor get specific Event")
    @Test
    void getEventId() throws Exception {

        EventDTO expectedEvent = EventDTO.builder().eventId(UUID.randomUUID()).description("test").title("ok").build();
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.title").value("ok"))
                .andExpect(jsonPath("$.description").value("test"));

        verify(eventservice, times(1)).getEvent(any());
    }

    @DisplayName("exhibitor cant get specific Event - BadRequest")
    @Test
    void cantGetEventId() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}", "random", UUID.randomUUID()))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("exhibitor get Application Details")
    @Test
    void exhibitorGetApplicationDetails() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}/application", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(""));

    }

    @DisplayName("exhibitor cant get Application Details on even - Not Found")
    @Test
    void exhibitorCantGetApplicationDetails() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/event/{eventId}/application", null, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @DisplayName("exhibitor apply for an event")
    @Test
    void exhibitorApplyForAnEvent() throws Exception {

        ApplicationRequest applicationRequest = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);
        UUID applicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        final ApplicationDTO expectedApplicationDTO = ApplicationDTO.builder()
                .applicationId(applicationId)
                .userId(userId)
                .eventId(eventId)
                .contactInformation("Hans Peter")
                .businessName("Hans Buerger")
                .typeOfBusiness(" foodie")
                .previousParticipation(false)
                .message("nimm mich")
                .status("IN_PROGRESS")
                .build();
        when(applicationservice.createApplication(any())).thenReturn(expectedApplicationDTO);

        mockMvc.perform(post("/exhibitor/{exhibitorId}/event/{eventId}/application", userId, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(applicationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value(applicationId.toString()))
                .andExpect(jsonPath("$.eventId").value(eventId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.businessName").value(expectedApplicationDTO.businessName()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @DisplayName("exhibitor apply for an event without applicationinformation - bad request")
    @Test
    void exhibitorApplyForAnEventWithoutApplicationInformation() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        mockMvc.perform(post("/exhibitor/{exhibitorId}/event/{eventId}/application", userId, eventId))
                .andExpect(status().isBadRequest());
    }

}

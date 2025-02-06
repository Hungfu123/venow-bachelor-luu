package org.venow.backend.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.config.TestSecurityConfig;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.exhibitor.ExhibitorController;
import org.venow.backend.web.request.ApplicationRequest;

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
import static org.venow.backend.TestBase.loadObjectFromJson;

@WebMvcTest(ExhibitorController.class)
@Import(TestSecurityConfig.class)
class ExhibitorApplicationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    Eventservice eventservice;

    @MockBean
    ApplicationService applicationservice;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("get Application Details")
    @Test
    void exhibitorGetApplicationDetails() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}/application", testuser.userId(), expectedEvent.eventId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(""));

    }

    @DisplayName("cant get Application Details on even - Not Found")
    @Test
    void exhibitorCantGetApplicationDetails() throws Exception {
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();

        mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}/application", null, expectedEvent.eventId()))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("apply for an event")
    @Test
    void exhibitorApplyForAnEvent() throws Exception {

        ApplicationRequest applicationRequest = loadObjectFromJson("/requests/applicationrequest.json", ApplicationRequest.class);

        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();

        final ApplicationDTO expectedApplicationDTO = ApplicationDTOFactory.createDefault().build();
        when(applicationservice.createApplication(any())).thenReturn(expectedApplicationDTO);

        mockMvc.perform(post("/exhibitor/{exhibitorId}/events/{eventId}/application", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(applicationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value(expectedApplicationDTO.getApplicationId().toString()))
                .andExpect(jsonPath("$.eventId").value(expectedApplicationDTO.getEventId().toString()))
                .andExpect(jsonPath("$.eventTitle").value(expectedApplicationDTO.getEventTitle()))
                .andExpect(jsonPath("$.exhibitorName").value(expectedApplicationDTO.getExhibitorName()))
                .andExpect(jsonPath("$.userId").value(expectedApplicationDTO.getUserId().toString()))
                .andExpect(jsonPath("$.businessName").value(expectedApplicationDTO.getBusinessName()))
                .andExpect(jsonPath("$.status").value(expectedApplicationDTO.getStatus()));
    }

    @DisplayName("apply for an event without application information - bad request")
    @Test
    void exhibitorApplyForAnEventWithoutApplicationInformation() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();

        mockMvc.perform(post("/exhibitor/{exhibitorId}/events/{eventId}/application", testuser.userId(), expectedEvent.eventId())
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("get Application Details Page")
    @Test
    void exhibitorGetApplicationDetailsPage() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        final ApplicationDTO expectedApplication = ApplicationDTOFactory.createDefault().build();
        when(applicationservice.getApplication(any())).thenReturn(expectedApplication);
        when(eventservice.getEvent(any())).thenReturn(expectedEvent);

        String contentAsString = mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}/application/{applicationId}", testuser.userId(), expectedEvent.eventId(), expectedApplication.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(applicationservice, times(1)).getApplication(any());

        final ApplicationDTO applicationDTO = objectMapper.readValue(contentAsString, ApplicationDTO.class);

        assertThat(applicationDTO.getApplicationId()).isEqualTo(expectedApplication.getApplicationId());
    }

    @DisplayName("get Application Details Page but gets Not Found")
    @Test
    void exhibitorGetApplicationDetailsPageNotFound() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        when(applicationservice.getApplication(any())).thenThrow(new NotFoundException("Application not found"));

        String contentAsString = mockMvc.perform(get("/exhibitor/{exhibitorId}/events/{eventId}/application/{applicationId}", testuser.userId(), expectedEvent.eventId(), UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("Application not found");
    }

    @DisplayName("Exhibitor get all Applications")
    @Test
    void exhibitorGetAllApplications() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().build();
        final List<ApplicationDTO> expectedApplications = Collections.singletonList(ApplicationDTOFactory.createDefault().build());
        when(applicationservice.getAllApplicationsByExhibitor(any())).thenReturn(expectedApplications);

        mockMvc.perform(get("/exhibitor/{exhibitorId}/applications", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicationId").value(expectedApplications.get(0).getApplicationId().toString()));


    }
    @DisplayName("Exhibitor cannot get applications when not existing")
    @Test
    void exhibitorCannotGetApplicationsWhenNotExisting() throws Exception {
        UUID nonExistentUserId = UUID.randomUUID();
        when(applicationservice.getAllApplicationsByExhibitor(any()))
                .thenThrow(new NotFoundException("Exhibitor not found for ID: " + nonExistentUserId));

        mockMvc.perform(get("/exhibitor/{exhibitorId}/applications", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Exhibitor not found for ID: " + nonExistentUserId));
    }

    @DisplayName("Exhibitor can see his application details")
    @Test
    void exhibitorCanSeeHisApplicationDetails() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        final ApplicationDTO expectedApplication = ApplicationDTOFactory.createDefault().build();
        when(applicationservice.getApplication(any())).thenReturn(expectedApplication);

        String contentAsString = mockMvc.perform(get("/exhibitor/{exhibitorId}/applications/{applicationId}", testuser.userId(),expectedApplication.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(applicationservice, times(1)).getApplication(any());

        final ApplicationDTO applicationDTO = objectMapper.readValue(contentAsString, ApplicationDTO.class);

        assertThat(applicationDTO.getApplicationId()).isEqualTo(expectedApplication.getApplicationId());
    }

    @DisplayName("Exhibitor cannot see a non-existent application")
    @Test
    void exhibitorCannotSeeNonExistentApplication() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withRole("EXHIBITOR").build();
        UUID nonExistentApplicationId = UUID.randomUUID();

        when(applicationservice.getApplication(any())).thenThrow(new NotFoundException("Application not found for ID: " + nonExistentApplicationId));

        mockMvc.perform(get("/exhibitor/{exhibitorId}/applications/{applicationId}", testuser.userId(), nonExistentApplicationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Application not found for ID: " + nonExistentApplicationId));
    }


}

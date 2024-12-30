package org.venow.backend.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.dto.EventDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.organizer.OrganizerEventController;
import org.venow.backend.web.request.ContractRequest;
import org.venow.backend.web.request.EventRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    @DisplayName("should return event details")
    public void test3() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault()
                .withOrganizerName(testuser.name())
                .build();

        when(eventService.getEvent(expectedEvent.eventId())).thenReturn(expectedEvent);

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/events/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EventDTO eventDTO = objectMapper.readValue(contentAsString, EventDTO.class);
        assertThat(eventDTO).usingRecursiveComparison().ignoringFields("organizerId").isEqualTo(expectedEvent);
    }

    @Test
    @DisplayName("cant find event")
    public void test4() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerId(testuser.userId()).build();

        when(eventService.getEvent(expectedEvent.eventId()))
                .thenThrow(new NotFoundException("Event not found"));

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/events/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("Event not found");
    }

    @Test
    @DisplayName("get on event create page")
    public void test5() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/events/create", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EventDTO eventDTO = objectMapper.readValue(contentAsString, EventDTO.class);
        assertThat(eventDTO.eventId()).isNull();
        assertThat(eventDTO.title()).isNull();
        assertThat(eventDTO.description()).isNull();
        assertThat(eventDTO.location()).isNull();
        assertThat(eventDTO.startDate()).isNull();
        assertThat(eventDTO.endDate()).isNull();
        assertThat(eventDTO.organizerName()).isNull();
        assertThat(eventDTO.organizerId()).isNull();
    }

    @Test
    @DisplayName("create an event")
    public void test6() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String eventRequest = objectMapper.writeValueAsString(new EventRequest("Test Event", "Test Description", "Duesseldorf", LocalDateTime.parse("2024-01-01T08:02"), LocalDateTime.parse("2024-01-03T08:02")));

        when(eventService.createEvent(any(),any())).thenReturn(expectedEvent);

        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/events/create", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final EventDTO eventDTO = objectMapper.readValue(contentAsString, EventDTO.class);
        assertThat(eventDTO).usingRecursiveComparison().ignoringFields("organizerId").isEqualTo(expectedEvent);
    }

    @Test
    @DisplayName("cant find organizerId")
    public void test7() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final String eventRequest = objectMapper.writeValueAsString(new EventRequest("Test Event", "Test Description", "Duesseldorf", LocalDateTime.parse("2024-01-01T08:02"), LocalDateTime.parse("2024-01-03T08:02")));

        when(eventService.createEvent(any(),any()))
                .thenThrow(new NotFoundException("OrganizerId not found"));

        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/events/create", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("OrganizerId not found");
    }

    @Test
    @DisplayName("Validation fails when mandatory fields are missing")
    public void testValidationFailsForMissingFields() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final String invalidEventRequest = objectMapper.writeValueAsString(
                new EventRequest("", "", "", null, null)
        );

        mockMvc.perform(post("/organizer/{organizerId}/events/create", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEventRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").value("must not be empty"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value("must not be empty"))
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.location").value("must not be empty"))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.startDate").value("must not be null"))
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.endDate").value("must not be null"));
    }

    @Test
    @DisplayName("Validation fails when endDate is before startDate")
    public void testValidationFailsForInvalidDateRange() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String invalidEventRequest = objectMapper.writeValueAsString(
                new EventRequest("Test Event", "Test Description", "Hoehle",
                        LocalDateTime.of(2024, 1, 2, 10, 0),
                        LocalDateTime.of(2024, 1, 1, 10, 0))
                );

        when(eventService.createEvent(any(),any())).thenReturn(expectedEvent);


        mockMvc.perform(post("/organizer/{organizerId}/events/create", testuser.userId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEventRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("End date must be after start date"));
    }

    @Test
    @DisplayName("edit an event")
    public void test8() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String eventRequest = objectMapper.writeValueAsString(new EventRequest("Test Event", "Test Description", "Duesseldorf", LocalDateTime.parse("2024-01-01T08:02"), LocalDateTime.parse("2024-01-03T08:02")));

        when(eventService.updateEvent(any(),any())).thenReturn(expectedEvent);

        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/events/edit/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final EventDTO eventDTO = objectMapper.readValue(contentAsString, EventDTO.class);
        assertThat(eventDTO).usingRecursiveComparison().ignoringFields("organizerId").isEqualTo(expectedEvent);
    }

    @Test
    @DisplayName("cant find organizerId")
    public void test9() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String eventRequest = objectMapper.writeValueAsString(new EventRequest("Test Event", "Test Description", "Duesseldorf", LocalDateTime.parse("2024-01-01T08:02"), LocalDateTime.parse("2024-01-03T08:02")));

        when(eventService.updateEvent(any(),any()))
                .thenThrow(new NotFoundException("OrganizerId not found"));

        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/events/edit/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("OrganizerId not found");
    }

    @Test
    @DisplayName("Validation fails when mandatory fields are missing")
    public void test10() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String invalidEventRequest = objectMapper.writeValueAsString(
                new EventRequest("", "", "", null, null)
        );

        mockMvc.perform(post("/organizer/{organizerId}/events/edit/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEventRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").value("must not be empty"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value("must not be empty"))
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.location").value("must not be empty"))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.startDate").value("must not be null"))
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.endDate").value("must not be null"));
    }

    @Test
    @DisplayName("Validation fails when endDate is before startDate")
    public void test11() throws Exception {
        final UserDTO testuser = UserDTOFactory.createDefault().withUserId(UUID.randomUUID()).build();
        final EventDTO expectedEvent = EventDTOFactory.createDefault().withOrganizerName(testuser.name()).build();
        final String invalidEventRequest = objectMapper.writeValueAsString(
                new EventRequest("Test Event", "Test Description", "Hoehle",
                        LocalDateTime.of(2024, 1, 2, 10, 0),
                        LocalDateTime.of(2024, 1, 1, 10, 0))
        );

        when(eventService.updateEvent(any(),any())).thenReturn(expectedEvent);

        mockMvc.perform(post("/organizer/{organizerId}/events/edit/{eventId}", testuser.userId(), expectedEvent.eventId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEventRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("End date must be after start date"));
    }
}

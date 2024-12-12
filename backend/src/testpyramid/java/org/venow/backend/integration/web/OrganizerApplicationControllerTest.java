package org.venow.backend.integration.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.ApplicationService;
import org.venow.backend.applicationservice.exception.AlreadyRejectedException;
import org.venow.backend.applicationservice.exception.InvalidInputException;
import org.venow.backend.domain.model.Status;
import org.venow.backend.testdata.ApplicationDataFactory;
import org.venow.backend.testdata.UserTestData;
import org.venow.backend.web.OrganizerApplicationController;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizerApplicationController.class)
class OrganizerApplicationControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService applicationService;


    @DisplayName("Organizer sees all his applications")
    @Test
    void organizerSeeAllHisApplication() throws Exception {
        ApplicationDTO defaultApplicationDTO = ApplicationDataFactory.createDefaultApplicationDTO();
        ApplicationDTO consultingApplicationDTO = ApplicationDataFactory.createConsultingApplicationDTO();
        ApplicationDTO itApplicationDTO = ApplicationDataFactory.createITApplicationDTO();
        final List<ApplicationDTO> expected = List.of(
                defaultApplicationDTO,
                consultingApplicationDTO,
                itApplicationDTO
        );

        when(applicationService.getAllApplicationsByOrganizer(any()))
                .thenReturn(expected);

        String expectedJson = objectMapper.writeValueAsString(expected);

        String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/applications", UserTestData.createOrganizer1().getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo(expectedJson);
    }

    @DisplayName("Organizer sees an empty list of applications")
    @Test
    void organizerSeeEmptyApplications() throws Exception {
        when(applicationService.getAllApplicationsByOrganizer(any()))
                .thenReturn(List.of());

        String expectedJson = objectMapper.writeValueAsString(List.of());

        String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/applications", UserTestData.createOrganizer1().getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo(expectedJson);
    }

    @DisplayName("Organizer gets BAD REQUEST for invalid input")
    @Test
    void organizerNotFound() throws Exception {
        when(applicationService.getAllApplicationsByOrganizer(any()))
                .thenThrow(new IllegalArgumentException("Organizer not found"));

        mockMvc.perform(get("/organizer/{organizerId}/applications", "invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Organizer gets NOT FOUND when no organizer exists")
    @Test
    void organizerBadRequest() throws Exception {
        mockMvc.perform(get("/organizer/{organizerId}/applications", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @DisplayName("Organizer gets BAD REQUEST for invalid applicationId format")
    @Test
    void organizerBadRequestInvalidApplicationIdFormat() throws Exception {
        mockMvc.perform(get("/organizer/{organizerId}/applications/{applicationId}",
                        UserTestData.createOrganizer1().getUserId(), "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Organizer sees a specific application")
    @Test
    void organizerSeeSpecificApplication() throws Exception, InvalidInputException {
        ApplicationDTO applicationDTO = ApplicationDataFactory.createDefaultApplicationDTO();

        when(applicationService.getApplication(any())).thenReturn(applicationDTO);

        String expectedJson = objectMapper.writeValueAsString(applicationDTO);

        String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/applications/{applicationId}",
                        UserTestData.createOrganizer1().getUserId(), applicationDTO.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo(expectedJson);
    }

    @DisplayName("Organizer does not see a non-existent application")
    @Test
    void organizerSeeNonExistentApplication() throws Exception, InvalidInputException {
        when(applicationService.getApplication(any()))
                .thenThrow(new IllegalArgumentException("Application not found"));

        mockMvc.perform(get("/organizer/{organizerId}/applications/{applicationId}",
                        UserTestData.createOrganizer1().getUserId(), UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Organizer gets NOT FOUND for empty organizerId")
    @Test
    void organizerEmptyOrganizerId() throws Exception {
        mockMvc.perform(get("/organizer/{organizerId}/applications", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Organizer reject application")
    @Test
    void organizeRejectApplication() throws Exception {
        ApplicationDTO applicationDTO = ApplicationDataFactory.createDefaultApplicationDTO();
        applicationDTO.setStatus("REJECTED");
        when(applicationService.rejectApplication(any()))
                .thenReturn(applicationDTO);


        String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/applications/{applicationId}/reject",
                        UserTestData.createOrganizer1().getUserId(), applicationDTO.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ApplicationDTO actualResponse = objectMapper.readValue(contentAsString, ApplicationDTO.class);

        assertThat(actualResponse.getStatus()).isEqualTo(Status.REJECTED.toString());
    }

    @DisplayName("organizer reject application which is already rejected")
    @Test
    void organizeRejectApplicationWhichIsAlreadyRejected() throws Exception {
        ApplicationDTO applicationDTO = ApplicationDataFactory.createDefaultApplicationDTO();
        applicationDTO.setStatus("REJECTED");
        when(applicationService.rejectApplication(any()))
                .thenThrow(new AlreadyRejectedException("Application already rejected"));

        String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/applications/{applicationId}/reject",
                        UserTestData.createOrganizer1().getUserId(), applicationDTO.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(contentAsString).isEqualTo("Application already rejected");
    }

}


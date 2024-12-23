package org.venow.backend.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.ContractService;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.*;
import org.venow.backend.service.exception.InvalidStatusException;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.ContractDTOFactory;
import org.venow.backend.testdata.EventDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.OrganizerContractController;
import org.venow.backend.web.reponse.ContractResponse;
import org.venow.backend.web.request.ContractRequest;

import java.util.List;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganizerContractController.class)
class OrganizerContractControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private Eventservice eventservice;
    @MockBean
    private UserService userService;
    @MockBean
    private ContractService contractService;

    @DisplayName("get on contract formular page und see contract response")
    @Test
    void getFormularContract() throws Exception {
        final String applicationName = "Hung Luu";
        final EventDTO event = EventDTOFactory.createDefault().build();
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();

        final ContractResponse contractDTO = new ContractResponse(
                application.getApplicationId(),
                organizer.userId(),
                application.getApplicationId(),
                applicationName,
                organizer.name(),
                event.title(),
                event.location(),
                event.startDate(),
                event.endDate(),
                application.getTypeOfBusiness(),
                application.getContactInformation(),
                application.getBusinessName(),
                "By accepting this contract, you agree to the terms and conditions of the event.",
                null,
                null,
                application.getStatus(),
                organizer.email()
        );
        final String expectedJson = objectMapper.writeValueAsString(contractDTO);
        when(applicationService.getApplication(any())).thenReturn(application);
        when(applicationService.getApplicationName(any())).thenReturn(applicationName);
        when(eventservice.getEvent(any())).thenReturn(event);
        when(userService.getUserByUserId(any())).thenReturn(organizer);

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/contracts/{applicationId}", organizer.userId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(contentAsString).whenIgnoringPaths("contractId").isEqualTo(expectedJson);
    }
    @DisplayName("get contract when applicationId is not found")
    @Test
    void getContractWhenApplicationIdNotFound() throws Exception {
        when(applicationService.getApplication(any())).thenThrow(new NotFoundException("Application not found"));

        mockMvc.perform(get("/organizer/{organizerId}/contracts/{applicationId}", UUID.randomUUID(), UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("get contract when event is not found")
    @Test
    void getContractWhenEventNotFound() throws Exception {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        when(applicationService.getApplication(any())).thenReturn(application);
        when(userService.getUserByUserId(any())).thenReturn(organizer);
        when(eventservice.getEvent(any())).thenThrow(new NotFoundException("Event not found"));

        mockMvc.perform(get("/organizer/{organizerId}/contracts/{applicationId}", organizer.userId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("submit contract and create contract")
    @Test
    void submitContractAndCreateContract() throws Exception, InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final ContractDTO contract = ContractDTOFactory.createDefault().build();

        when(contractService.createContract(any(), any(), any(), any())).thenReturn(contract);

        ContractRequest contractRequest = new ContractRequest(new PriceDto(5000), "A50");

        String contractRequestJson = objectMapper.writeValueAsString(contractRequest);


        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/contracts/{applicationId}", application.getUserId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractRequestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(contentAsString).and(
                jsonNode -> jsonNode.node("contractId").isNotNull(),
                jsonNode -> jsonNode.node("price").isEqualTo(50.0),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("A50")
        );
    }

    @DisplayName("submit contract with invalid data - BAD REQUEST")
    @Test
    void submitContractWithInvalidData() throws Exception {
        final String invalidContractRequestJson = "{}";

        mockMvc.perform(post("/organizer/{organizerId}/contracts/{applicationId}", UUID.randomUUID(), UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContractRequestJson))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("validate price and booth number")
    @Test
    void validatePriceAndBoothNumber() throws Exception, InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final ContractDTO contract = ContractDTOFactory.createDefault().build();

        when(contractService.createContract(any(), any(), any(), any())).thenReturn(contract);

        ContractRequest contractRequest = new ContractRequest(null, null);

        String contractRequestJson = objectMapper.writeValueAsString(contractRequest);


        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/contracts/{applicationId}", application.getUserId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractRequestJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(contentAsString).and(
                jsonNode -> jsonNode.node("price").isEqualTo("Price must not be null"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("Stand number must not be empty")
        );
    }

    @DisplayName("validate price and booth number empty - BAD REQUEST")
    @Test
    void validatePriceAndBoothNumber2() throws Exception, InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        final ContractDTO contract = ContractDTOFactory.createDefault().build();

        when(contractService.createContract(any(), any(), any(), any())).thenReturn(contract);

        ContractRequest contractRequest = new ContractRequest(new PriceDto(0), "");

        String contractRequestJson = objectMapper.writeValueAsString(contractRequest);


        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/contracts/{applicationId}", application.getUserId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractRequestJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(contentAsString).and(
                jsonNode -> jsonNode.node("price.amountInEuro").isEqualTo("Price must be greater than 0"),
                jsonNode -> jsonNode.node("boothNumber").isEqualTo("Stand number must not be empty")
        );
    }

    @DisplayName("should show Bad Request when status is wrong - BAD REQUEST")
    @Test
    void shouldshowBadRequestWhenStatusIsWrong() throws Exception, InvalidStatusException {
        final ApplicationDTO application = ApplicationDTOFactory.createDefault().withStatus(Status.CONTRACT.name()).build();

        ContractRequest contractRequest = new ContractRequest(new PriceDto(50000), "A50");

        String contractRequestJson = objectMapper.writeValueAsString(contractRequest);


        when(contractService.createContract(any(), any(), any(), any())).thenThrow(new InvalidStatusException("Application Status must be in progress"));


        final String contentAsString = mockMvc.perform(post("/organizer/{organizerId}/contracts/{applicationId}", application.getUserId(), application.getApplicationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractRequestJson))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(contentAsString).isEqualTo("Application Status must be in progress");
    }

    @DisplayName("see all contracts by organizer")
    @Test
    void seeAllContractsByOrganizer() throws Exception {
        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final ContractDTO contract = ContractDTOFactory.createDefault().build();
        final ContractDTO contract2 = ContractDTOFactory.createDefault().build();

        List<ContractDTO> contractList = List.of(contract, contract2);
        when(contractService.getAllContractsByOrganizer(any())).thenReturn(contractList);

        final String contentAsString = mockMvc.perform(get("/organizer/{organizerId}/contracts", organizer.userId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(contentAsString).isArray().hasSize(2);
    }
}

package org.venow.backend.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.ContractService;
import org.venow.backend.service.Eventservice;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.PriceDto;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.testdata.ContractDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;
import org.venow.backend.web.ExhibitorContractController;
import org.venow.backend.web.reponse.ContractResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExhibitorContractController.class)
public class ExhibitorContractControllerTest {
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


    @DisplayName("exhibitor get on contract details")
    @Test
    public void getContractDetails() throws Exception {

        final UserDTO organizer = UserDTOFactory.createDefault().build();
        final ContractDTO contract = ContractDTOFactory.createDefault()
                .withContractId("9a330d97-f4fb-4596-ab33-b826a0919272")
                .withOrganizer(organizer)
                .build();

        when(contractService.getContractByApplicationIdAndContactId(contract.getApplication().getApplicationId(), contract.getContractId())).thenReturn(contract);

        final String contentAsString = mockMvc.perform(get("/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}", organizer.userId(), contract.getApplication().getApplicationId(), contract.getContractId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(
                contentAsString
        ).isEqualTo(
                objectMapper.writeValueAsString(ContractResponse.builder()
                        .contractId(UUID.fromString("9a330d97-f4fb-4596-ab33-b826a0919272"))
                        .organizerId(organizer.userId())
                        .exhibitorId(contract.getExhibitor().userId())
                        .applicationId(contract.getApplication().getApplicationId())
                        .applicantName("Default Applicant")
                        .organizerName("Default Organizer")
                        .eventName("Default Event")
                        .location("Default Location")
                        .startDate(LocalDateTime.parse("2023-01-01T10:00:00"))
                        .endDate(LocalDateTime.parse("2023-01-02T18:00:00"))
                        .typeOfBusiness("Food Truck")
                        .contactInformation("example@example.com")
                        .businessName("Sample Business")
                        .terms("Default terms and conditions.")
                        .price(new PriceDto(BigDecimal.valueOf(50)))
                        .boothNumber("A50")
                        .status("CONTRACT")
                        .organizerEmail("organizer@example.com")
                        .build()

                )
        );
    }
    @DisplayName("exhibitor get on contract details - invalid inputs")
    @Test
    public void getContractDetails_InvalidInputs() throws Exception {
        mockMvc.perform(get("/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}",
                        "invalid-uuid", "invalid-uuid", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("exhibitor get on contract details - contract not found")
    @Test
    public void getContractDetails_NotFound() throws Exception {
        UUID contractId = UUID.randomUUID();
        UUID applicationId = UUID.randomUUID();
        UUID exhibitorId = UUID.randomUUID();

        when(contractService.getContractByApplicationIdAndContactId(applicationId, contractId))
                .thenThrow(new NotFoundException("Contract not found"));

        mockMvc.perform(get("/exhibitor/{exhibitorId}/applications/{applicationId}/contract-details/{contractId}",
                        exhibitorId, applicationId, contractId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }



}

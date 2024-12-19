package org.venow.backend.testdata;

import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.PriceDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ContractDTOFactory {

    public static ContractDTOWith createDefault() {
        ContractDTO defaultContract = ContractDTO.builder()
                .contractId(UUID.randomUUID())
                .organizerId(UUID.randomUUID())
                .applicantName("Default Applicant")
                .organizerName("Default Organizer")
                .eventName("Default Event")
                .location("Default Location")
                .startDate(String.valueOf(LocalDateTime.of(2023, 1, 1, 10, 0)))
                .endDate(String.valueOf(LocalDateTime.of(2023, 1, 2, 18, 0)))
                .typeOfBusiness("Default Business Type")
                .contactInformation("contact@example.com")
                .businessName("Default Business")
                .terms("Default terms and conditions.")
                .price(new PriceDto(5000))
                .boothNumber("A50")
                .status("CONTRACT")
                .organizerEmail("organizer@example.com")
                .build();

        return new ContractDTOWith(defaultContract);
    }

    public static class ContractDTOWith {
        private ContractDTO contractDTO;

        public ContractDTOWith(ContractDTO contractDTO) {
            this.contractDTO = contractDTO;
        }

        public ContractDTOWith withContractId(String contractId) {
            this.contractDTO = contractDTO.toBuilder().contractId(UUID.fromString(contractId)).build();
            return this;
        }

        public ContractDTOWith withOrganizerId(String organizerId) {
            this.contractDTO = contractDTO.toBuilder().organizerId(UUID.fromString(organizerId)).build();
            return this;
        }

        public ContractDTOWith withApplicantName(String applicantName) {
            this.contractDTO = contractDTO.toBuilder().applicantName(applicantName).build();
            return this;
        }

        public ContractDTOWith withOrganizerName(String organizerName) {
            this.contractDTO = contractDTO.toBuilder().organizerName(organizerName).build();
            return this;
        }

        public ContractDTOWith withEventName(String eventName) {
            this.contractDTO = contractDTO.toBuilder().eventName(eventName).build();
            return this;
        }

        public ContractDTOWith withLocation(String location) {
            this.contractDTO = contractDTO.toBuilder().location(location).build();
            return this;
        }

        public ContractDTOWith withStartDate(String startDate) {
            this.contractDTO = contractDTO.toBuilder().startDate(startDate).build();
            return this;
        }

        public ContractDTOWith withEndDate(String endDate) {
            this.contractDTO = contractDTO.toBuilder().endDate(endDate).build();
            return this;
        }

        public ContractDTOWith withTypeOfBusiness(String typeOfBusiness) {
            this.contractDTO = contractDTO.toBuilder().typeOfBusiness(typeOfBusiness).build();
            return this;
        }

        public ContractDTOWith withContactInformation(String contactInformation) {
            this.contractDTO = contractDTO.toBuilder().contactInformation(contactInformation).build();
            return this;
        }

        public ContractDTOWith withBusinessName(String businessName) {
            this.contractDTO = contractDTO.toBuilder().businessName(businessName).build();
            return this;
        }

        public ContractDTOWith withTerms(String terms) {
            this.contractDTO = contractDTO.toBuilder().terms(terms).build();
            return this;
        }

        public ContractDTOWith withPrice(int price) {
            this.contractDTO = contractDTO.toBuilder().price(new PriceDto(price)).build();
            return this;
        }

        public ContractDTOWith withBoothNumber(String boothNumber) {
            this.contractDTO = contractDTO.toBuilder().boothNumber(boothNumber).build();
            return this;
        }

        public ContractDTOWith withStatus(String status) {
            this.contractDTO = contractDTO.toBuilder().status(status).build();
            return this;
        }

        public ContractDTOWith withOrganizerEmail(String organizerEmail) {
            this.contractDTO = contractDTO.toBuilder().organizerEmail(organizerEmail).build();
            return this;
        }

        public ContractDTO build() {
            return this.contractDTO;
        }
    }
}

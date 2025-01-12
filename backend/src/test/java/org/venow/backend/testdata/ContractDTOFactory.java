package org.venow.backend.testdata;

import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.PriceDto;
import org.venow.backend.service.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContractDTOFactory {

    public static ContractDTOWith createDefault() {
        ContractDTO defaultContract = ContractDTO.builder()
                .contractId(UUID.randomUUID())
                .organizer(UserDTOFactory.createDefault().build())
                .exhibitor(UserDTOFactory.createDefault().withRole("EXHIBITOR").build())
                .application(ApplicationDTOFactory.createDefault().withUserId(UUID.randomUUID().toString()).build())
                .applicantName("Default Applicant")
                .organizerName("Default Organizer")
                .eventName("Default Event")
                .location("Default Location")
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0).toString())
                .endDate(LocalDateTime.of(2023, 1, 2, 18, 0).toString())
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

        public ContractDTOWith withOrganizer(UserDTO organizer) {
            this.contractDTO = contractDTO.toBuilder().organizer(organizer).build();
            return this;
        }

        public ContractDTOWith withExhibitor(UserDTO exhibitor) {
            this.contractDTO = contractDTO.toBuilder().exhibitor(exhibitor).build();
            return this;
        }

        public ContractDTOWith withApplication(ApplicationDTO application) {
            this.contractDTO = contractDTO.toBuilder().application(application).build();
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

package org.venow.backend.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@Setter
public class ContractDTO {
    private final Long id;
    private final UUID contractId;
    private final UUID organizerId;
    private final ApplicationDTO application;
    private final String applicantName;
    private final String organizerName;
    private final String eventName;
    private final String location;
    private final String startDate;
    private final String endDate;
    private final String terms;
    private final PriceDto price;
    private final String boothNumber;
    private final String status;
    private final String organizerEmail;

}

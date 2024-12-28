package org.venow.backend.service.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@Setter
public class ContractDTO {
    private Long id;
    private UUID contractId;
    private UserDTO organizer;
    private UserDTO exhibitor;
    private ApplicationDTO application;
    private String applicantName;
    private String organizerName;
    private String eventName;
    private String location;
    private String startDate;
    private String endDate;
    private String terms;
    private PriceDto price;
    private String boothNumber;
    private String status;
    private String organizerEmail;

}

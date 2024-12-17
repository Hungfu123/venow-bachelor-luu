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
    private final String terms;
    private final PriceDto price;
    private final String status;

}

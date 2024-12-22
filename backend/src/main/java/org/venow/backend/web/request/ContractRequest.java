package org.venow.backend.web.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.venow.backend.service.dto.PriceDto;

public record ContractRequest (
        @NotNull(message = "Price must not be null")
        @Valid PriceDto price,
        @NotEmpty(message = "Stand number must not be empty") String boothNumber){
}
